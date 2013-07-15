package dev.sdb.client.presenter;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.ReleaseSearchView;
import dev.sdb.client.view.SearchView;
import dev.sdb.client.view.desktop.UiFactoryImpl;
import dev.sdb.client.view.desktop.detail.DetailWidget;
import dev.sdb.client.view.desktop.detail.ReleaseDetailWidget;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public class ReleasePresenter extends AbstractBrowsePresenter implements ReleaseSearchView.Presenter {

	public ReleasePresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.RELEASE, Flavor.RELEASES);
	}



	@Override protected DetailWidget createDetailWidget() {
		final ReleaseDetailWidget widget = new ReleaseDetailWidget(this);
		DataGrid<Entity> table = widget.getSublist().getTable();

		UiFactoryImpl.addReleaseMusicColumns(table, true, true);

		table.setWidth("100%");

		// Set the total row count. You might send an RPC request to determine the
		// total row count.
		table.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		int rangeLength = 10;
		table.setVisibleRange(0, rangeLength);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSequenceListFromServer(widget);
			}
		};
		dataProvider.addDataDisplay(table);

		// Add a ColumnSortEvent.AsyncHandler to connect sorting to the
		// AsyncDataPRrovider.
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);

		final SingleSelectionModel<Entity> selectionModel = new SingleSelectionModel<Entity>();
		table.setSelectionModel(selectionModel);

		table.addDomHandler(new DoubleClickHandler() {
			@Override public void onDoubleClick(final DoubleClickEvent event) {
				Soundtrack soundtrack = (Soundtrack) selectionModel.getSelectedObject();
				if (soundtrack != null) {
					Music music = soundtrack.getMusic();
					if (music != null) {
						addHistoryNavigation(ContentPresenterType.MUSIC, music);
					}
				}
			}
		}, DoubleClickEvent.getType());

		return widget;
	}

	public void getSequenceListFromServer(final ReleaseDetailWidget detailWidget) {
		final SublistWidget list = detailWidget.getSublist();

		Release release = (Release) detailWidget.getCurrentEntity();
		if (release == null) {
			list.setElementVisibility(-1);
			return;
		}

		final long audioId = release.getAudioId();

		//if there's no id, cancel the action
		if (audioId <= 0) {
			list.setElementVisibility(-1);
			return;
		}

		final DataGrid<Entity> table = list.getTable();
		final Range range = table.getVisibleRange();

		SearchServiceAsync service = getClientFactory().getSearchService();

		// Then, we send the input to the server.
		service.getReleaseSoundtrackList(audioId, range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				int total = searchResult.getTotalLength();

				String resultInfo = "";
				if (total == 0) {
					resultInfo = "Für diese Veröffentlichung existieren keine Sequenzen.";
				} else if (total > 0) {
					resultInfo = "Für diese Veröffentlichung " + (total == 1 ? "existiert 1 Sequenz" : ("existieren " + total + " Sequenzen")) + ".";
				}

				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());

				list.setSelectionInfoText(resultInfo);
				list.setElementVisibility(total);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showRpcError(caught, "Sequence list for [" + Flavor.RELEASES.name() + "] audioId=" + audioId, null);

				list.setElementVisibility(-1);
			}
		});
	}

	protected SearchView createQueryWidget(String term) {
		// Create the query widget instance
		final ReleaseSearchView queryWidget = getClientFactory().getUi().getReleaseSearchView();
		queryWidget.setPresenter(this);

		// Set the search term
		queryWidget.setText(term);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSearchFromServer(queryWidget);
			}
		};

		queryWidget.setDataProvider(dataProvider);

		return queryWidget;
	}



}

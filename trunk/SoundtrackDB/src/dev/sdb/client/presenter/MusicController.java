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
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.MusicDetailView;
import dev.sdb.client.view.MusicSearchView;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicController extends AbstractDataController implements MusicSearchView.Presenter,
		MusicDetailView.Presenter {

	public MusicController(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.MUSIC, Flavor.MUSIC);
	}

	@Override protected void addSearchResultColumns(DataGrid<Entity> table) {
		addMusicColumns(table, isSearchResultCompactView(), true);
	}


	@Override protected DetailView createDetailWidget() {
		final MusicDetailView view = getClientFactory().getUi().getMusicDetailView();
		view.setPresenter(this);
		initMusicReleaseListTable(view);
		return view;
	}

	private void initMusicReleaseListTable(final MusicDetailView widget) {
		DataGrid<Entity> table = widget.getSublist().getTable();

		addReleaseColumns(table, true, true);

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
				getMusicReleaseListFromServer(widget);
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
				Entity entity = selectionModel.getSelectedObject();
				if (entity != null) {
					addHistoryNavigation(ContentPresenterType.RELEASE, entity);
				}
			}
		}, DoubleClickEvent.getType());
	}

	public void getMusicReleaseListFromServer(MusicDetailView detailWidget) {
		final SublistWidget list = detailWidget.getSublist();

		Music music = (Music) getCurrentEntity();
		if (music == null) {
			list.setElementVisibility(-1);
			return;
		}

		final long id = music.getId();

		//if there's no id, cancel the action
		if (id <= 0) {
			list.setElementVisibility(-1);
			return;
		}

		final DataGrid<Entity> table = list.getTable();
		final Range range = table.getVisibleRange();

		// Then, we send the input to the server.
		SEARCH_SERVICE.getMusicReleaseList(id, range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				int total = searchResult.getTotalLength();

				String resultInfo = "";
				if (total == 0) {
					resultInfo = "Für diese Musik sind keine Veröffentlichungen bekannt.";
				} else if (total > 0) {
					resultInfo = "Für diese Musik " + (total == 1 ? "ist 1 Veröffentlichung" : ("sind " + total + " Veröffentlichungen")) + " bekannt.";
				}

				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());

				list.setSelectionInfoText(resultInfo);
				list.setElementVisibility(total);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showRpcError(caught, "Release list for [" + Flavor.MUSIC.name() + "] id=" + id, null);

				list.setElementVisibility(-1);
			}
		});

	}


}

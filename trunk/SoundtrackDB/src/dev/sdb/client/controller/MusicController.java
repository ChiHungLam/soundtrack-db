package dev.sdb.client.controller;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.detail.MusicDetailWidget;
import dev.sdb.client.ui.detail.sublist.ReleaseList;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicController extends AbstractDataController {

	public MusicController(SoundtrackDB sdb) {
		super(sdb, ControllerType.MUSIC, Flavor.MUSIC);
	}

	@Override protected void addSearchResultColumns(CellTable<Entity> table) {
		addMusicColumns(table);
	}


	@Override protected DetailWidget createDetailWidget() {
		final MusicDetailWidget widget = new MusicDetailWidget(this);
		initMusicReleaseListTable(widget);
		return widget;
	}

	private void initMusicReleaseListTable(final MusicDetailWidget widget) {
		CellTable<Entity> table = widget.getReleaseList().getTable();

		addReleaseColumns(table);

		table.setWidth("100%", true);

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
					addHistoryNavigation(ControllerType.RELEASE, entity);
				}
			}
		}, DoubleClickEvent.getType());
	}

	public void getMusicReleaseListFromServer(MusicDetailWidget detailWidget) {
		final ReleaseList list = detailWidget.getReleaseList();

		Music music = (Music) detailWidget.getCurrentEntity();
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

		final CellTable<Entity> table = list.getTable();
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

				list.setResultInfoText(resultInfo);
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

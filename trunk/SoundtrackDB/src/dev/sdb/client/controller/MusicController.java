package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

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
		final MusicDetailWidget widget = new MusicDetailWidget();
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

	}

	public void getMusicReleaseListFromServer(MusicDetailWidget detailWidget) {
		final ReleaseList list = detailWidget.getReleaseList();

		Music music = (Music) detailWidget.getCurrentEntity();
		if (music == null) {
			list.clearTable();
			return;
		}

		final long versionId = music.getId();

		//if there's no id, cancel the action
		if (versionId <= 0) {
			list.clearTable();
			return;
		}

		final CellTable<Entity> table = list.getTable();
		final Range range = table.getVisibleRange();

		// Then, we send the input to the server.
		SEARCH_SERVICE.getMusicReleaseList(versionId, range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				int total = searchResult.getTotalLength();

				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();

				list.clearTable();

				showRpcError(caught, "Release list for [" + Flavor.MUSIC.name() + "] id=" + versionId, null);
			}
		});

	}


}

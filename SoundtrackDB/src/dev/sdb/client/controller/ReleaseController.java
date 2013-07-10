package dev.sdb.client.controller;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.detail.ReleaseDetailWidget;
import dev.sdb.client.ui.detail.sublist.SequenceList;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public class ReleaseController extends AbstractDataController {

	public ReleaseController(SoundtrackDB sdb) {
		super(sdb, ControllerType.RELEASE, Flavor.RELEASES);
	}

	@Override protected void addSearchResultColumns(CellTable<Entity> table) {
		addReleaseColumns(table);
	}

	@Override protected DetailWidget createDetailWidget() {
		final ReleaseDetailWidget widget = new ReleaseDetailWidget();
		CellTable<Entity> table = widget.getSequenceList().getTable();

		addReleaseMusicColumns(table);

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
						History.newItem(ControllerType.MUSIC.getToken() + "?id=" + music.getId());
					}
				}
			}
		}, DoubleClickEvent.getType());

		return widget;
	}

	public void getSequenceListFromServer(final ReleaseDetailWidget detailWidget) {
		final SequenceList list = detailWidget.getSequenceList();

		Release release = (Release) detailWidget.getCurrentEntity();
		if (release == null) {
			list.clearTable();
			return;
		}

		final long audioId = release.getAudioId();

		//if there's no id, cancel the action
		if (audioId <= 0) {
			list.clearTable();
			return;
		}

		final CellTable<Entity> table = list.getTable();
		final Range range = table.getVisibleRange();

		// Then, we send the input to the server.
		SEARCH_SERVICE.getReleaseSoundtrackList(audioId, range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				int total = searchResult.getTotalLength();

				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();

				list.clearTable();

				showRpcError(caught, "Sequence list for [" + Flavor.RELEASES.name() + "] audioId=" + audioId, null);
			}
		});
	}

}

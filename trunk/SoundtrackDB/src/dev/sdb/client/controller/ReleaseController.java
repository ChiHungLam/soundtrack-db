package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.detail.ReleaseDetailWidget;
import dev.sdb.client.ui.detail.sublist.SequenceList;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public class ReleaseController extends AbstractSearchController {

	public ReleaseController(SoundtrackDB sdb) {
		super(sdb, ControllerType.RELEASE, Flavor.RELEASES);
	}

	@Override protected Column<Entity, ?> createRendererColumn() {
		final TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getTitle();
			}
		};
		return column;
	}

	@Override protected DetailWidget createDetailWidget() {
		final ReleaseDetailWidget widget = new ReleaseDetailWidget();

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSequenceListFromServer(widget);
			}
		};
		widget.getSequenceList().init(createSequenceListColumn(), dataProvider);

		return widget;
	}

	protected Column<Entity, ?> createSequenceListColumn() {
		final TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Soundtrack) entity).toString();
			}
		};
		return column;
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

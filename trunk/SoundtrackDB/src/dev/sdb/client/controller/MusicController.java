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
import dev.sdb.client.ui.detail.MusicDetailWidget;
import dev.sdb.client.ui.detail.sublist.ReleaseList;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;

public class MusicController extends AbstractSearchController {

	public MusicController(SoundtrackDB sdb) {
		super(sdb, ControllerType.MUSIC, Flavor.MUSIC);
	}

	@Override protected Column<Entity, ?> createRendererColumn() {
		TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getTitle();
			}
		};
		return column;
	}

	@Override protected DetailWidget createDetailWidget() {
		final MusicDetailWidget widget = new MusicDetailWidget();

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getReleaseListFromServer(widget);
			}
		};
		widget.getReleaseList().init(createReleaseListColumn(), dataProvider);

		return widget;
	}

	private Column<Entity, ?> createReleaseListColumn() {
		final TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getTitle();
			}
		};
		return column;
	}

	public void getReleaseListFromServer(MusicDetailWidget detailWidget) {
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

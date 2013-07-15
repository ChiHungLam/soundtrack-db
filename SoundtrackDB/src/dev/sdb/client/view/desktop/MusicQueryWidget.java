package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.MusicQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class MusicQueryWidget extends QueryWidget implements MusicQueryView {

	public MusicQueryWidget() {
		super();
	}

	@Override public void setPresenter(MusicQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addMusicColumns(table, isSearchResultCompactView(), true);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.MUSIC;
	}
}

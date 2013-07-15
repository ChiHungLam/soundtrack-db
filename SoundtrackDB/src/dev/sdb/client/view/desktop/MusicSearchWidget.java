package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.view.MusicSearchView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class MusicSearchWidget extends QueryWidget implements MusicSearchView {

	public MusicSearchWidget() {
		super();
	}

	@Override public void setPresenter(MusicSearchView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addMusicColumns(table, isSearchResultCompactView(), true);
	}
}

package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.view.SoundtrackSearchView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackSearchWidget extends QueryWidget implements SoundtrackSearchView {

	public SoundtrackSearchWidget() {
		super();
	}

	@Override public void setPresenter(SoundtrackSearchView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addSoundtrackColumns(table, isSearchResultCompactView(), true);
	}
}

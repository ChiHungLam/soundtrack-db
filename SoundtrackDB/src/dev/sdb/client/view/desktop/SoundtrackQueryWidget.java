package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SoundtrackQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackQueryWidget extends QueryWidget implements SoundtrackQueryView {

	public SoundtrackQueryWidget() {
		super();
	}

	@Override public void setPresenter(SoundtrackQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addSoundtrackColumns(table, isSearchResultCompactView(), true);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.SOUNDTRACK;
	}
}

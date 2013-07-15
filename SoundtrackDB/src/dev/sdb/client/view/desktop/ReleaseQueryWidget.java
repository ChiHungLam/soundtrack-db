package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.ReleaseQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class ReleaseQueryWidget extends QueryWidget implements ReleaseQueryView {

	public ReleaseQueryWidget() {
		super();
	}

	@Override public void setPresenter(ReleaseQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addReleaseColumns(table, isSearchResultCompactView(), true);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.RELEASE;
	}
}

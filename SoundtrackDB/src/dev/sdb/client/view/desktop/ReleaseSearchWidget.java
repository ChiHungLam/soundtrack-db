package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.view.ReleaseSearchView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class ReleaseSearchWidget extends QueryWidget implements ReleaseSearchView {

	public ReleaseSearchWidget() {
		super();
	}

	@Override public void setPresenter(ReleaseSearchView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addReleaseColumns(table, isSearchResultCompactView(), true);
	}
}

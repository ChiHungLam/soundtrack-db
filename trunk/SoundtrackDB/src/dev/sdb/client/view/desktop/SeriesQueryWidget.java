package dev.sdb.client.view.desktop;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SeriesQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;
import dev.sdb.shared.model.entity.Entity;

public class SeriesQueryWidget extends QueryWidget implements SeriesQueryView {

	public SeriesQueryWidget() {
		super();
	}

	@Override public void setPresenter(SeriesQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	protected void addSearchResultColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addSeriesColumns(table, isSearchResultCompactView(), true);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.SERIES;
	}
}

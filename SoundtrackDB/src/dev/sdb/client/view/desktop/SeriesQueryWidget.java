package dev.sdb.client.view.desktop;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SeriesQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;

public class SeriesQueryWidget extends QueryWidget implements SeriesQueryView {

	public SeriesQueryWidget() {
		super();
	}

	@Override public void setPresenter(SeriesQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.SERIES;
	}
}

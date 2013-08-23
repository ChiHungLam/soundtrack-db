package dev.sdb.client.view.desktop;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.ReleaseQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;

public class ReleaseQueryWidget extends QueryWidget implements ReleaseQueryView {

	public ReleaseQueryWidget() {
		super();
	}

	@Override public void setPresenter(ReleaseQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.RELEASE;
	}
}

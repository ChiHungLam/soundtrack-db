package dev.sdb.client.view.desktop;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.MusicQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;

public class MusicQueryWidget extends QueryWidget implements MusicQueryView {

	public MusicQueryWidget() {
		super();
	}

	@Override public void setPresenter(MusicQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.MUSIC;
	}
}

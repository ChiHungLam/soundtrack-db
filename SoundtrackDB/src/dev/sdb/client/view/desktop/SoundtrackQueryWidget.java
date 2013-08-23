package dev.sdb.client.view.desktop;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SoundtrackQueryView;
import dev.sdb.client.view.desktop.search.QueryWidget;

public class SoundtrackQueryWidget extends QueryWidget implements SoundtrackQueryView {

	public SoundtrackQueryWidget() {
		super();
	}

	@Override public void setPresenter(SoundtrackQueryView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected ContentPresenterType getContentPresenterType() {
		return ContentPresenterType.SOUNDTRACK;
	}
}

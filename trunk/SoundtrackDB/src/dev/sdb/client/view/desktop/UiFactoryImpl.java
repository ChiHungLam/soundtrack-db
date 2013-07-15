package dev.sdb.client.view.desktop;

import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.UiFactory;

public class UiFactoryImpl implements UiFactory {

	private NavigatorView navigatorView;

	public UiFactoryImpl() {
		super();
	}

	@Override public NavigatorView getNavigatorView() {
		if (this.navigatorView == null)
			this.navigatorView = new NavigatorWidget();
		return this.navigatorView;
	}
}

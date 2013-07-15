package dev.sdb.client.view.desktop;

import dev.sdb.client.view.HomeView;
import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.UiFactory;

public class UiFactoryImpl implements UiFactory {

	private NavigatorView navigatorView;
	private HomeView homeView;

	public UiFactoryImpl() {
		super();
	}

	@Override public NavigatorView getNavigatorView() {
		if (this.navigatorView == null)
			this.navigatorView = new NavigatorWidget();
		return this.navigatorView;
	}

	@Override public HomeView getHomeView() {
		if (this.homeView == null)
			this.homeView = new HomeWidget();
		return this.homeView;
	}
}

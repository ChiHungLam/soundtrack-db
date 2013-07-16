package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.NavigatorView;

public class NavigatorPresenter implements NavigatorView.Presenter {

	private ClientFactory clientFactory;

	public NavigatorPresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override public void switchToArea(ContentPresenterType type) {
		NavigatorView view = this.clientFactory.getUi().getNavigatorView();
		view.highlightLink(type);
	}

}

package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.NavigatorView;

public class NavigatorPresenter implements NavigatorView.Presenter {

	// Used to obtain views, eventBus
	// Alternatively, could be injected via GIN
	private ClientFactory clientFactory;


	public NavigatorPresenter(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public void start() {
		NavigatorView view = this.clientFactory.getUi().getNavigatorView();
		view.setPresenter(this);
	}

}

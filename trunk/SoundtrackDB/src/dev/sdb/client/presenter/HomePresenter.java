package dev.sdb.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.HomeView;

public class HomePresenter implements HomeView.Presenter {

	private ClientFactory clientFactory;

	public HomePresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override public ContentPresenterType getType() {
		return ContentPresenterType.HOME;
	}

	@Override public IsWidget getWidget(String state) {
		HomeView view = this.clientFactory.getUi().getHomeView();
		view.setPresenter(this);
		return view;
	}

}

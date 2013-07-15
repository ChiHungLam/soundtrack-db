package dev.sdb.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.HomeView;

public class HomePresenter extends AbstractContentPresenter implements HomeView.Presenter {

	public HomePresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.HOME);
	}

	@Override public IsWidget getView(String state) {
		HomeView view = getClientFactory().getUi().getHomeView();
		view.setPresenter(this);
		return view;
	}

}

package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.HeaderView;

public class HeaderPresenter implements HeaderView.Presenter {

	private ClientFactory clientFactory;

	public HeaderPresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override public void switchToArea(ContentPresenterType type) {
		HeaderView view = this.clientFactory.getUi().getHeaderView();
		view.setAreaInfo(type);
	}

}

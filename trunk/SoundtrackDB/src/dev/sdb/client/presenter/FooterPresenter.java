package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.FooterView;

public class FooterPresenter implements FooterView.Presenter {

	private ClientFactory clientFactory;

	public FooterPresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override public void switchToArea(ContentPresenterType type) {
		FooterView view = this.clientFactory.getUi().getFooterView();
		view.highlightLink(type);
	}

}

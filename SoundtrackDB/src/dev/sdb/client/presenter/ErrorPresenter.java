package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.ErrorView;

public class ErrorPresenter implements ErrorView.Presenter {



	private ClientFactory clientFactory;

	public ErrorPresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override public void showError(String title, String message, Throwable caught) {
		String details = "";

		if (caught != null)
			details = "<hr><br><i>Original error message was:</i><br><br>" + caught.getLocalizedMessage();

		ErrorView view = this.clientFactory.getUi().getErrorView();

		view.getTitleWidget().setText(title);
		view.getSummaryWidget().setHTML(message);
		view.getDetailsWidget().setHTML(details);
		view.show();
	}

}

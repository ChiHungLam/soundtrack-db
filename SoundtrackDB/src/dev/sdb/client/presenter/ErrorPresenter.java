package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.ErrorView;

public class ErrorPresenter implements ErrorView.Presenter {

	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private ClientFactory clientFactory;

	public ErrorPresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override public void showError(String title, String message, Throwable caught) {

		//		this.clientFactory.getUi().showRpcError(caught, message, view);

		message = SERVER_ERROR + "<br><br>" + message;
		message += "<br><br>Original error message:<br>" + caught.getLocalizedMessage();

		ErrorView view = this.clientFactory.getUi().getErrorView();
		view.getTitleWidget().setText(title);
		view.getMessageWidget().setHTML(message);
		view.show();
	}

}

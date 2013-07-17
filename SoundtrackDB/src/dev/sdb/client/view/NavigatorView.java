package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.presenter.ContentPresenterType;

public interface NavigatorView extends IsWidget {

	public interface Presenter {
		void switchToArea(ContentPresenterType type);
	}

	void setPresenter(Presenter presenter);

	/**
	 * @param type
	 *            The currently active type
	 */
	void highlightLink(ContentPresenterType type);
}

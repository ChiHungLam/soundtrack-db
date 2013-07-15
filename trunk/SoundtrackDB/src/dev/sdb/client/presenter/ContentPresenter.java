package dev.sdb.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;

public interface ContentPresenter {

	/**
	 * @param state
	 * @return the view for the specified state
	 */
	IsWidget getView(String state);

	/**
	 * @return the type
	 */
	ContentPresenterType getType();
}

package dev.sdb.client.controller;

import com.google.gwt.user.client.ui.Widget;

public interface Controller {
	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	public static final int VISIBLE_RANGE_LENGTH = 20;

	Widget getWidget(String state);

	/**
	 * @return the type
	 */
	ControllerType getType();
}

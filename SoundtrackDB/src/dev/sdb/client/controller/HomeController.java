package dev.sdb.client.controller;

import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.ui.HomeWidget;

public class HomeController implements Controller {

	private HomeWidget homeWidget;

	public HomeController() {
		super();
	}

	@Override public Widget getWidget(String state) {
		if (this.homeWidget == null)
			this.homeWidget = new HomeWidget();
		return this.homeWidget;
	}

}

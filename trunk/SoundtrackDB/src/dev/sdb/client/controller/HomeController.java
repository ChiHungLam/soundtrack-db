package dev.sdb.client.controller;

import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.HomeWidget;

public class HomeController implements Controller {

	@SuppressWarnings("unused") private SoundtrackDB sdb;

	private HomeWidget homeWidget;

	public HomeController(SoundtrackDB sdb) {
		super();
		this.sdb = sdb;
	}

	@Override public ControllerType getType() {
		return ControllerType.HOME;
	}

	@Override public Widget getWidget(String state) {
		if (this.homeWidget == null)
			this.homeWidget = new HomeWidget();
		return this.homeWidget;
	}

}

package dev.sdb.client.presenter;

import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.desktop.HomeWidget;

public class HomeController implements Controller {

	@SuppressWarnings("unused") private ClientFactory clientFactory;

	private HomeWidget homeWidget;

	public HomeController(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
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

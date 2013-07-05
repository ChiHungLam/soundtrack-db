package dev.sdb.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.Controller;
import dev.sdb.client.controller.ControllerType;
import dev.sdb.client.controller.HomeController;
import dev.sdb.client.controller.MusicController;
import dev.sdb.client.controller.ReleaseController;
import dev.sdb.client.controller.SoundtrackController;
import dev.sdb.client.ui.NavigatorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	private static final Map<ControllerType, Controller> CONTROLLER_MAP = new HashMap<ControllerType, Controller>();
	
	private String token;

	public SoundtrackDB() {
		super();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//attaching navigator
		Widget navigatorWidget = createNavigatorWidget();
		RootPanel.get("navigator_area").add(navigatorWidget);

		//attaching content
		String startingToken = getStartingToken();
		setContentArea(startingToken);

		//preparing history support
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if (getToken().equals(historyToken))
					return;

				setContentArea(historyToken);
			}
		});
	}

	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	private void setContentArea(String token) {
		setToken(token);

		final RootPanel contentArea = RootPanel.get("content_area");

		while (contentArea.getWidgetCount() > 0)
			contentArea.remove(0);

		Widget contentWidget = getContentWidget(token);
		if (contentWidget == null)
			return;

		contentArea.add(contentWidget);
	}

	private String getStartingToken() {
		String href = Window.Location.getHref();
		System.out.println("Starting href: " + href);
		int pos = href.indexOf("#");

		String token;
		if (pos == -1)
			token = "";
		else
			token = href.substring(pos + 1);

		//		if (token.isEmpty()) {
		//			token = ControllerType.HOME.getToken();
		//			History.newItem(token, false);
		//		}

		return token;
	}

	protected Widget getContentWidget(String historyToken) {
		ControllerType type = getControllerType(historyToken);

		Controller controller = getController(type);
		if (controller == null)
			return null;

		String state = getControllerState(type, historyToken);
		Widget widget = controller.getWidget(state);

		return widget;
	}

	private String getControllerState(ControllerType type, String historyToken) {
		assert (type != null);

		if (historyToken == null || historyToken.isEmpty())
			return "";

		int pos = historyToken.indexOf("?");
		int tokenLength = historyToken.length();

		if (pos == 0) {
			if (tokenLength == 1)
				return "";
			return historyToken.substring(1);
		}

		String token = type.getToken();

		if (!historyToken.startsWith(token)) {
			System.err.println("given ControllerType '" + type + "' does not match historyToken '" + historyToken + "'");
			return "";
		}

		if (pos == -1 || pos == (tokenLength - 1))
			return "";

		return historyToken.substring(pos + 1);
	}

	private ControllerType getControllerType(String historyToken) {
		ControllerType type = ControllerType.getByToken(historyToken);
		if (type == null)
			type = ControllerType.HOME;
		return type;
	}

	protected Controller getController(ControllerType type) {
		assert (type != null);

		Controller controller = CONTROLLER_MAP.get(type);
		if (controller != null)
			return controller;

		switch (type) {
		case HOME:
			controller = new HomeController(this);
			break;
		case RELEASE:
			controller = new ReleaseController(this);
			break;
		case MUSIC:
			controller = new MusicController(this);
			break;
		case SOUNDTRACK:
			controller = new SoundtrackController(this); // not yet
			break;
		case SERIES:
			controller = null; // not yet
			break;

		default:
			System.err.println("unknown ControllerType: " + type);
			// load the home page
			return getController(ControllerType.HOME);
		}

		if (controller == null)
			return null;

		CONTROLLER_MAP.put(type, controller);
		return controller;
	}

	private Widget createNavigatorWidget() {
		return new NavigatorWidget();
	}


}

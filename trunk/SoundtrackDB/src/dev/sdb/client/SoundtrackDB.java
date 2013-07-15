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

import dev.sdb.client.presenter.Controller;
import dev.sdb.client.presenter.ControllerType;
import dev.sdb.client.presenter.HomeController;
import dev.sdb.client.presenter.MusicController;
import dev.sdb.client.presenter.ReleaseController;
import dev.sdb.client.presenter.SoundtrackController;
import dev.sdb.client.ui.NavigatorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	private static final Map<ControllerType, Controller> CONTROLLER_MAP = new HashMap<ControllerType, Controller>();

	private static boolean LOG_URLS_AND_TOKENS = false;

	/**
	 * This token represents the current state of the application. Its value is <i>not</i> url encoded.
	 */
	private String token;

	public SoundtrackDB() {
		super();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		if (LOG_URLS_AND_TOKENS) {
			String href = Window.Location.getHref();
			System.out.println("Start up url: " + href);
		}

		//attaching navigator
		Widget navigatorWidget = createNavigatorWidget();
		RootPanel.get("navigator_area").add(navigatorWidget);

		//attaching content
		String startingToken = History.getToken();
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

	public void setHistory(String token, boolean issueEvent) {
		if (LOG_URLS_AND_TOKENS) {
			System.out.println("Adding to history" + (!issueEvent ? " (without events)" : "") + ": " + token);
		}

		History.newItem(token, issueEvent);
		if (!issueEvent)
			setToken(token);
	}

	private String getToken() {
		return this.token;
	}

	private void setToken(String token) {
		if (LOG_URLS_AND_TOKENS) {
			System.out.println("Setting token to: " + token);
		}

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
			controller = new SoundtrackController(this);
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

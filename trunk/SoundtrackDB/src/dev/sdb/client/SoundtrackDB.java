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
import dev.sdb.client.controller.MusicController;
import dev.sdb.client.controller.ReleaseController;
import dev.sdb.client.ui.NavigatorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	private String currentContent;
	private static final Map<ControllerType, Controller> CONTROLLER_MAP = new HashMap<ControllerType, Controller>();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		String startingContent = getStartingContent();
		
		Widget navigatorWidget = createNavigatorWidget();
		Widget contentWidget = getContentWidget(startingContent);

		final RootPanel contentArea = RootPanel.get("content_area");
		final RootPanel navigatorArea = RootPanel.get("navigator_area");

		contentArea.add(contentWidget);
		navigatorArea.add(navigatorWidget);

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if (SoundtrackDB.this.currentContent.equals(historyToken))
					return;

				if (contentArea.getWidgetCount() > 0)
					contentArea.remove(0);

				Widget widget = getContentWidget(historyToken);
				if (widget != null)
					contentArea.add(widget);
			}
		});
	}

	private String getStartingContent() {
		String href = Window.Location.getHref();
		System.out.println(href);
		int pos = href.indexOf("#");
		if (pos == -1)
			return "search";

		return href.substring(pos + 1);
	}

	protected Widget getContentWidget(String historyToken) {
		this.currentContent = historyToken;

		ControllerType type = getControllerType(historyToken);
		String state = getControllerState(type, historyToken);

		Controller controller = getController(type);
		if (controller == null)
			return null;

		Widget widget = controller.getWidget(state);

		return widget;
	}

	private String getControllerState(ControllerType type, String historyToken) {
		assert (type != null);

		if (historyToken == null || historyToken.isEmpty())
			return null;

		if (historyToken.startsWith("?")) {
			if (historyToken.length() == 1)
				return null;
			return historyToken.substring(1);
		}

		String token = type.getToken();

		if (!historyToken.startsWith(token)) {
			System.err.println("given ControllerType '" + type + "' does not match historyToken '" + historyToken + "'");
			return null;
		}

		int pos = historyToken.indexOf("?");
		if (pos == -1 || pos == (historyToken.length() - 1))
			return null;

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

			break;
		case RELEASE:
			controller = new ReleaseController();
			break;
		case MUSIC:
			controller = new MusicController();
			break;
		case SOUNDTRACK:
			controller = null; // not yet
			break;
		case SERIES:
			controller = null; // not yet
			break;

		default:
			System.err.println("unknown ControllerType: " + type);
			// load the home page
			return getController(ControllerType.HOME);
		}

		// "search"
		// "music"
		// "release"
		// "series"

		if (controller == null)
			return null;

		CONTROLLER_MAP.put(type, controller);
		return controller;
	}
	private Widget createNavigatorWidget() {
		return new NavigatorWidget();
	}
}

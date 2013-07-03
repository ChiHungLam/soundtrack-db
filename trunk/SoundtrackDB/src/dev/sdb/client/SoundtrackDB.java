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
import dev.sdb.client.controller.MusicController;
import dev.sdb.client.controller.QueryController;
import dev.sdb.client.controller.ReleaseController;
import dev.sdb.client.ui.NavigatorWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	private String currentContent;
	private static final Map<String, Controller> CONTROLLER_MAP = new HashMap<String, Controller>();
	
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

		Controller controller = getController(this.currentContent);
		if (controller == null)
			return null;

		//		String parameters = parseParament;
		Widget widget = controller.getWidget();

		return widget;
	}

	private Controller getController(String historyToken) {
		Controller controller = CONTROLLER_MAP.get(historyToken);
		if (controller != null)
			return controller;

		// Parse the history token
		if (historyToken.startsWith("search")) {
			controller = new QueryController();

		} else if (historyToken.startsWith("music")) {
			controller = new MusicController();

		} else if (historyToken.startsWith("release")) {
			controller = new ReleaseController();

		} else if (historyToken.startsWith("series")) {
			controller = null;
		}
		if (controller == null)
			return null;

		CONTROLLER_MAP.put(this.currentContent, controller);
		return controller;
	}
	private Widget createNavigatorWidget() {
		return new NavigatorWidget();
	}
}

package dev.sdb.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

import dev.sdb.client.presenter.ContentPresenter;
import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.presenter.HomePresenter;
import dev.sdb.client.presenter.MusicController;
import dev.sdb.client.presenter.ReleaseController;
import dev.sdb.client.presenter.SoundtrackController;
import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.UiFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	private static final Map<ContentPresenterType, ContentPresenter> CONTROLLER_MAP = new HashMap<ContentPresenterType, ContentPresenter>();

	private ClientFactory clientFactory;

	public SoundtrackDB() {
		super();
	}

	private void logStartup() {
		String href = Window.Location.getHref();
		System.out.println("Start up url: " + href);
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		logStartup();

		// Create ClientFactory using deferred binding so we can replace with different
		// impls in gwt.xml

		UiFactory uiFactory = GWT.create(UiFactory.class);
		this.clientFactory = new ClientFactory(uiFactory);

		//final RootPanel contentArea = RootPanel.get("content_area");
		final RootPanel navigatorArea = RootPanel.get("navigator_area");

		//attaching navigator
		NavigatorView navigatorWidget = this.clientFactory.getUi().getNavigatorView();
		navigatorArea.add(navigatorWidget);

		//attaching content
		String startingToken = History.getToken();
		setContentArea(startingToken);

		//preparing history support
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if (SoundtrackDB.this.clientFactory.getHistoryManager().getToken().equals(historyToken))
					return;

				setContentArea(historyToken);
			}
		});
	}



	private void setContentArea(String token) {
		this.clientFactory.getHistoryManager().setToken(token);

		final RootPanel contentArea = RootPanel.get("content_area");

		while (contentArea.getWidgetCount() > 0)
			contentArea.remove(0);

		IsWidget contentWidget = getContentWidget(token);
		if (contentWidget == null)
			return;

		contentArea.add(contentWidget);
	}

	protected IsWidget getContentWidget(String historyToken) {
		ContentPresenterType type = getControllerType(historyToken);

		ContentPresenter controller = getController(type);
		if (controller == null)
			return null;

		String state = getControllerState(type, historyToken);
		IsWidget widget = controller.getWidget(state);

		return widget;
	}

	private String getControllerState(ContentPresenterType type, String historyToken) {
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

	private ContentPresenterType getControllerType(String historyToken) {
		ContentPresenterType type = ContentPresenterType.getByToken(historyToken);
		if (type == null)
			type = ContentPresenterType.HOME;
		return type;
	}

	protected ContentPresenter getController(ContentPresenterType type) {
		assert (type != null);

		ContentPresenter controller = CONTROLLER_MAP.get(type);
		if (controller != null)
			return controller;

		switch (type) {
		case HOME:
			controller = new HomePresenter(this.clientFactory);
			break;
		case RELEASE:
			controller = new ReleaseController(this.clientFactory);
			break;
		case MUSIC:
			controller = new MusicController(this.clientFactory);
			break;
		case SOUNDTRACK:
			controller = new SoundtrackController(this.clientFactory);
			break;
		case SERIES:
			controller = null; // not yet
			break;

		default:
			System.err.println("unknown ControllerType: " + type);
			// load the home page
			return getController(ContentPresenterType.HOME);
		}

		if (controller == null)
			return null;

		CONTROLLER_MAP.put(type, controller);
		return controller;
	}



}
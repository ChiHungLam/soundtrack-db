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
import dev.sdb.client.presenter.MusicPresenter;
import dev.sdb.client.presenter.ReleasePresenter;
import dev.sdb.client.presenter.SeriesPresenter;
import dev.sdb.client.presenter.SoundtrackPresenter;
import dev.sdb.client.view.UiFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	private static final Map<ContentPresenterType, ContentPresenter> PRESENTER_MAP = new HashMap<ContentPresenterType, ContentPresenter>();

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

		//attaching navigator
		IsWidget navigatorWidget = uiFactory.getNavigatorView();
		RootPanel.get("navigator_area").add(navigatorWidget);

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
		ContentPresenterType type = getContentPresenterType(historyToken);

		ContentPresenter controller = getContentPresenter(type);
		if (controller == null)
			return null;

		String state = getContentPresenterState(type, historyToken);
		IsWidget widget = controller.getView(state);

		return widget;
	}

	private String getContentPresenterState(ContentPresenterType type, String historyToken) {
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

	private ContentPresenterType getContentPresenterType(String historyToken) {
		ContentPresenterType type = ContentPresenterType.getByToken(historyToken);
		if (type == null)
			type = ContentPresenterType.HOME;
		return type;
	}

	protected ContentPresenter getContentPresenter(ContentPresenterType type) {
		assert (type != null);

		ContentPresenter presenter = PRESENTER_MAP.get(type);
		if (presenter != null)
			return presenter;

		switch (type) {
		case HOME:
			presenter = new HomePresenter(this.clientFactory);
			break;
		case RELEASE:
			presenter = new ReleasePresenter(this.clientFactory);
			break;
		case MUSIC:
			presenter = new MusicPresenter(this.clientFactory);
			break;
		case SOUNDTRACK:
			presenter = new SoundtrackPresenter(this.clientFactory);
			break;
		case SERIES:
			presenter = new SeriesPresenter(this.clientFactory);
			break;

		default:
			System.err.println("unknown ContentPresenterType: " + type);
			// load the home page
			return getContentPresenter(ContentPresenterType.HOME);
		}

		//		if (presenter == null)
		//			return null;

		PRESENTER_MAP.put(type, presenter);
		return presenter;
	}
}

package dev.sdb.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

import dev.sdb.client.presenter.ContentPresenter;
import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.presenter.HomePresenter;
import dev.sdb.client.presenter.MusicPresenter;
import dev.sdb.client.presenter.ReleasePresenter;
import dev.sdb.client.presenter.SeriesPresenter;
import dev.sdb.client.presenter.SoundtrackPresenter;

public class HistoryManager {

	private static final Map<ContentPresenterType, ContentPresenter> PRESENTER_MAP = new HashMap<ContentPresenterType, ContentPresenter>();

	private static boolean LOG_URLS_AND_TOKENS = false;

	private ClientFactory clientFactory;

	/**
	 * This token represents the current state of the application. Its value is <i>not</i> url encoded.
	 */
	private String token;

	public HistoryManager(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	public void setUp() {
		//Prepare history event handler
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if (getToken().equals(historyToken))
					return;

				setContentArea(historyToken);
			}
		});

		//Show current content
		String startingToken = History.getToken();
		setContentArea(startingToken);
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

	private void setContentArea(String token) {
		setToken(token);

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

	public void setHistory(String token, boolean issueEvent) {
		if (LOG_URLS_AND_TOKENS) {
			System.out.println("Adding to history" + (!issueEvent ? " (without events)" : "") + ": " + token);
		}

		History.newItem(token, issueEvent);
		if (!issueEvent)
			setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		if (LOG_URLS_AND_TOKENS) {
			System.out.println("Setting token to: " + token);
		}

		this.token = token;
	}
}

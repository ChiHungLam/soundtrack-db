package dev.sdb.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.event.ContentAreaChangeEvent;
import dev.sdb.client.presenter.CatalogPresenter;
import dev.sdb.client.presenter.ContentPresenter;
import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.presenter.HomePresenter;
import dev.sdb.client.presenter.MusicPresenter;
import dev.sdb.client.presenter.ReleasePresenter;
import dev.sdb.client.presenter.SeriesPresenter;
import dev.sdb.client.presenter.SoundtrackPresenter;

public class HistoryManager {

	private static final Logger LOGGER = Logger.getLogger(HistoryManager.class.getName());

	private static final Map<ContentPresenterType, ContentPresenter> PRESENTER_MAP = new HashMap<ContentPresenterType, ContentPresenter>();

	private ClientFactory clientFactory;

	/**
	 * This token represents the current state of the application. Its value is <i>not</i> url encoded.
	 */
	private String token;

	public HistoryManager(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
		setUp();
	}

	private void setUp() {
		//Prepare history event handler
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if (getToken().equals(historyToken))
					return;

				setContentArea(historyToken);
			}
		});
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
		case CATALOG:
			presenter = new CatalogPresenter(this.clientFactory);
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

		ContentPresenterType type = getContentPresenterType(token);
		IsWidget contentWidget = getContentWidget(type, token);

		this.clientFactory.getEventBus().fireEventFromSource(new ContentAreaChangeEvent(type, contentWidget), this);
	}

	protected IsWidget getContentWidget(ContentPresenterType type, String historyToken) {
		ContentPresenter presenter = getContentPresenter(type);
		if (presenter == null)
			return null;

		String state = getContentPresenterState(type, historyToken);
		IsWidget widget = presenter.getView(state);

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

	public void createHistory(String token, boolean issueEvent) {
		LOGGER.config("Adding to history" + (!issueEvent ? " (without events)" : "") + ": " + token);

		History.newItem(token, issueEvent);
		if (!issueEvent)
			setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		LOGGER.config("Setting token to: " + token);

		this.token = token;
	}

	public void handleCurrentHistory() {
		//Show current content
		String startingToken = History.getToken();
		setContentArea(startingToken);
	}
}

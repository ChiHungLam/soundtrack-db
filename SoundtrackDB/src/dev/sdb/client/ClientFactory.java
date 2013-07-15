package dev.sdb.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.EventBus;

import dev.sdb.client.service.SearchService;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.UiFactory;

public class ClientFactory {

	private static final EventBus eventBus = new SimpleEventBus();

	private static SearchServiceAsync searchService;
	private static final HistoryManager HISTORY_MANAGER = new HistoryManager();

	private final UiFactory uiFactory;

	public ClientFactory(UiFactory uiFactory) {
		super();
		this.uiFactory = uiFactory;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public SearchServiceAsync getSearchService() {
		if (searchService == null)
			searchService = GWT.create(SearchService.class);
		return searchService;
	}

	public UiFactory getUi() {
		return this.uiFactory;
	}

	public HistoryManager getHistoryManager() {
		return HISTORY_MANAGER;
	}
}

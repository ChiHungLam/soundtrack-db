package dev.sdb.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.EventBus;

import dev.sdb.client.service.SearchService;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.UiFactory;

public class ClientFactory {

	private final EventBus eventBus = new SimpleEventBus();
	private final UiFactory uiFactory;

	private HistoryManager historyManager;
	private SearchServiceAsync searchService;

	public ClientFactory(UiFactory uiFactory) {
		super();
		this.uiFactory = uiFactory;
	}

	public UiFactory getUi() {
		return this.uiFactory;
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	public HistoryManager getHistoryManager() {
		if (this.historyManager == null)
			this.historyManager = new HistoryManager(this);
		return this.historyManager;
	}

	public SearchServiceAsync getSearchService() {
		if (this.searchService == null)
			this.searchService = GWT.create(SearchService.class);
		return this.searchService;
	}
}

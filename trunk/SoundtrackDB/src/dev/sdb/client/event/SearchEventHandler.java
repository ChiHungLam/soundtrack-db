package dev.sdb.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface SearchEventHandler extends EventHandler {
	void onSearch(SearchEvent event);
}

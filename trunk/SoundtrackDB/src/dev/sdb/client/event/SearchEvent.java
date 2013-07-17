package dev.sdb.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SearchEvent extends GwtEvent<SearchEventHandler> {

	public static Type<SearchEventHandler> TYPE = new Type<SearchEventHandler>();

	private final String searchTerm;

	public SearchEvent(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	@Override public GwtEvent.Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override protected void dispatch(SearchEventHandler handler) {
		handler.onSearch(this);
	}

	public String getSearchTerm() {
		return this.searchTerm;
	}
}

package dev.sdb.client.ui.search;

import com.google.gwt.event.shared.GwtEvent;

public class SearchEvent extends GwtEvent<SearchEventHandler> {

	public static Type<SearchEventHandler> TYPE = new Type<SearchEventHandler>();

	private final String text;

	//	private final SearchScope range;

	public SearchEvent(String text) { // SearchScope range
		this.text = text;
		//		this.range = range;
	}

	@Override public GwtEvent.Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override protected void dispatch(SearchEventHandler handler) {
		handler.onSearch(this);
	}

	//	public SearchScope getRange() {
	//		return this.range;
	//	}

	public String getText() {
		return this.text;
	}
}

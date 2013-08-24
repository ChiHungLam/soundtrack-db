package dev.sdb.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SearchEvent extends GwtEvent<SearchEventHandler> {

	public enum Mode {
		VALID,
		INVALID
	}

	public static Type<SearchEventHandler> TYPE = new Type<SearchEventHandler>();

	private final Mode mode;
	private final String value;

	public SearchEvent(Mode mode, String value) {
		this.mode = mode;
		this.value = value;
	}

	@Override public GwtEvent.Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override protected void dispatch(SearchEventHandler handler) {
		handler.onSearch(this);
	}

	public Mode getMode() {
		return this.mode;
	}

	public String getValue() {
		return this.value;
	}
}

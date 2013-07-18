package dev.sdb.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class FatalErrorEvent extends GwtEvent<FatalErrorEventHandler> {

	public static Type<FatalErrorEventHandler> TYPE = new Type<FatalErrorEventHandler>();

	private final String title;
	private final String message;
	private final Throwable throwable;

	public FatalErrorEvent(String title, String message, Throwable throwable) {
		super();
		this.title = title;
		this.message = message;
		this.throwable = throwable;
	}

	@Override public GwtEvent.Type<FatalErrorEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override protected void dispatch(FatalErrorEventHandler handler) {
		handler.onFatalError(this);
	}

	public String getTitle() {
		return this.title;
	}

	public String getMessage() {
		return this.message;
	}

	public Throwable getThrowable() {
		return this.throwable;
	}
}

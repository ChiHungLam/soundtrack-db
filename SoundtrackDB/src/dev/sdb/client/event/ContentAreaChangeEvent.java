package dev.sdb.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.presenter.ContentPresenterType;

public class ContentAreaChangeEvent extends GwtEvent<ContentAreaChangeEventHandler> {

	public static Type<ContentAreaChangeEventHandler> TYPE = new Type<ContentAreaChangeEventHandler>();

	private final ContentPresenterType contentPresenterType;
	private final IsWidget contentWidget;

	public ContentAreaChangeEvent(ContentPresenterType contentPresenterType, IsWidget contentWidget) {
		super();
		this.contentPresenterType = contentPresenterType;
		this.contentWidget = contentWidget;
	}

	@Override public GwtEvent.Type<ContentAreaChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override protected void dispatch(ContentAreaChangeEventHandler handler) {
		handler.onChange(this);
	}

	public ContentPresenterType getContentPresenterType() {
		return this.contentPresenterType;
	}

	public IsWidget getContentWidget() {
		return this.contentWidget;
	}
}

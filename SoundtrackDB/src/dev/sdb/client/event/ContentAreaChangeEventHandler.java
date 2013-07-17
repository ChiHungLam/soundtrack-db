package dev.sdb.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ContentAreaChangeEventHandler extends EventHandler {
	void onChange(ContentAreaChangeEvent event);
}
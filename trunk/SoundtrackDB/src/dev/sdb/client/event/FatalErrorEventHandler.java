package dev.sdb.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface FatalErrorEventHandler extends EventHandler {
	void onFatalError(FatalErrorEvent event);
}
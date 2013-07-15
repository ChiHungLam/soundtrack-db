package dev.sdb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

import dev.sdb.client.view.UiFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SoundtrackDB implements EntryPoint {

	public SoundtrackDB() {
		super();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		System.out.println("Start up url: " + Window.Location.getHref());

		// Create ClientFactory using deferred binding so we can replace with different
		// impls in gwt.xml
		UiFactory uiFactory = GWT.create(UiFactory.class);
		ClientFactory clientFactory = new ClientFactory(uiFactory);

		//attaching navigator
		IsWidget navigatorWidget = uiFactory.getNavigatorView();
		RootPanel.get("navigator_area").add(navigatorWidget);

		//Setting up history and displayed content
		clientFactory.getHistoryManager().setUp();
	}

}

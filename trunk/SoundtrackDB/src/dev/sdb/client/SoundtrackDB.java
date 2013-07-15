package dev.sdb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

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

		//Create the client factory
		ClientFactory clientFactory = new ClientFactory();

		//Attach the navigator to the corresponding panel
		IsWidget navigator = clientFactory.getUi().getNavigatorView();
		RootPanel.get("navigator_area").add(navigator);

		//Setting up history and display the current content
		clientFactory.getHistoryManager().setUp();
	}

}

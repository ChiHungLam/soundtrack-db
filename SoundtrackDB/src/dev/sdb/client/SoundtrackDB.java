package dev.sdb.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import dev.sdb.client.presenter.FooterPresenter;
import dev.sdb.client.presenter.HeaderPresenter;
import dev.sdb.client.presenter.NavigatorPresenter;
import dev.sdb.client.view.FooterView;
import dev.sdb.client.view.HeaderView;
import dev.sdb.client.view.NavigatorView;

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

		//Create the navigator
		NavigatorPresenter navigatorPresenter = new NavigatorPresenter(clientFactory);
		NavigatorView navigatorView = clientFactory.getUi().getNavigatorView();
		navigatorView.setPresenter(navigatorPresenter);
		//Attach the navigator to the corresponding panel
		RootPanel.get("navigator_area").add(navigatorView);

		//Create the header
		HeaderPresenter headerPresenter = new HeaderPresenter(clientFactory);
		HeaderView headerView = clientFactory.getUi().getHeaderView();
		headerView.setPresenter(headerPresenter);
		//Attach the header to the corresponding panel
		RootPanel.get("header_area").add(headerView);

		//Create the footer
		FooterPresenter footerPresenter = new FooterPresenter(clientFactory);
		FooterView footerView = clientFactory.getUi().getFooterView();
		footerView.setPresenter(footerPresenter);
		//Attach the footer to the corresponding panel
		RootPanel.get("footer_area").add(footerView);


		//Setting up history and display the current content
		clientFactory.getHistoryManager().setUp(navigatorPresenter, headerPresenter, footerPresenter);
	}

}

package dev.sdb.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

import dev.sdb.client.event.ContentAreaChangeEvent;
import dev.sdb.client.event.ContentAreaChangeEventHandler;
import dev.sdb.client.presenter.ContentPresenterType;
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

	private static final Logger LOGGER = Logger.getLogger(SoundtrackDB.class.getName());

	public SoundtrackDB() {
		super();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		LOGGER.info("Starting up with url: " + Window.Location.getHref());

		//Create the client factory
		ClientFactory clientFactory = new ClientFactory();

		//Create the navigator
		final NavigatorPresenter navigatorPresenter = new NavigatorPresenter(clientFactory);
		NavigatorView navigatorView = clientFactory.getUi().getNavigatorView();
		navigatorView.setPresenter(navigatorPresenter);
		//Attach the navigator to the corresponding panel
		RootPanel.get("navigator_area").add(navigatorView);

		//Create the header
		final HeaderPresenter headerPresenter = new HeaderPresenter(clientFactory);
		HeaderView headerView = clientFactory.getUi().getHeaderView();
		headerView.setPresenter(headerPresenter);
		//Attach the header to the corresponding panel
		RootPanel.get("header_area").add(headerView);

		//Create the footer
		final FooterPresenter footerPresenter = new FooterPresenter(clientFactory);
		FooterView footerView = clientFactory.getUi().getFooterView();
		footerView.setPresenter(footerPresenter);
		//Attach the footer to the corresponding panel
		RootPanel.get("footer_area").add(footerView);

		HistoryManager historyManager = clientFactory.getHistoryManager();

		clientFactory.getEventBus().addHandler(ContentAreaChangeEvent.TYPE, new ContentAreaChangeEventHandler() {
			@Override public void onChange(ContentAreaChangeEvent event) {
				ContentPresenterType type = event.getContentPresenterType();
				IsWidget contentWidget = event.getContentWidget();

				navigatorPresenter.switchToArea(type);
				headerPresenter.switchToArea(type);
				footerPresenter.switchToArea(type);

				final RootPanel contentArea = RootPanel.get("content_area");

				while (contentArea.getWidgetCount() > 0)
					contentArea.remove(0);

				if (contentWidget != null)
					contentArea.add(contentWidget);
			}
		});

		historyManager.handleCurrentHistory();

		//Setting up history and display the current content

	}

	public static void setBrowserWindowTitle(String title) {
		if (Document.get() != null) {
			title = "sdb" + (title == null || title.isEmpty() ? "" : (" - " + title));
			Document.get().setTitle(title);
		}
	}

}

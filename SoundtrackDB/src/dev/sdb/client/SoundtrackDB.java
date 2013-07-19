package dev.sdb.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;

import dev.sdb.client.event.ContentAreaChangeEvent;
import dev.sdb.client.event.ContentAreaChangeEventHandler;
import dev.sdb.client.event.FatalErrorEvent;
import dev.sdb.client.event.FatalErrorEventHandler;
import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.presenter.ErrorPresenter;
import dev.sdb.client.presenter.FooterPresenter;
import dev.sdb.client.presenter.HeaderPresenter;
import dev.sdb.client.presenter.NavigatorPresenter;
import dev.sdb.client.presenter.SectionInfoPresenter;
import dev.sdb.client.view.ErrorView;
import dev.sdb.client.view.FooterView;
import dev.sdb.client.view.HeaderView;
import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.SectionInfoView;

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

		//Create the navigator presenter
		final NavigatorPresenter navigatorPresenter = new NavigatorPresenter(clientFactory);
		NavigatorView navigatorView = clientFactory.getUi().getNavigatorView();
		navigatorView.setPresenter(navigatorPresenter);
		//Attach the view to the corresponding panel
		RootPanel.get("navigator_area").add(navigatorView);

		//Create the header presenter
		final HeaderPresenter headerPresenter = new HeaderPresenter(clientFactory);
		//Create view and attach it to the corresponding panel
		HeaderView headerView = clientFactory.getUi().getHeaderView();
		headerView.setPresenter(headerPresenter);
		RootPanel.get("header_area").add(headerView);

		//Create the footer presenter
		final FooterPresenter footerPresenter = new FooterPresenter(clientFactory);
		//Create view and attach it to the corresponding panel
		FooterView footerView = clientFactory.getUi().getFooterView();
		footerView.setPresenter(footerPresenter);
		RootPanel.get("footer_area").add(footerView);

		//Create the footer presenter
		final SectionInfoPresenter sectionInfoPresenter = new SectionInfoPresenter(clientFactory);
		//Create view and attach it to the corresponding panel
		SectionInfoView sectionInfoView = clientFactory.getUi().getSectionInfoView();
		sectionInfoView.setPresenter(sectionInfoPresenter);
		RootPanel.get("sectioninfo_area").add(sectionInfoView);

		//Create the error presenter
		final ErrorPresenter errorPresenter = new ErrorPresenter(clientFactory);
		//Create view and attach it to the corresponding panel
		ErrorView errorView = clientFactory.getUi().getErrorView();
		errorView.setPresenter(errorPresenter);
		RootPanel.get("error_area").add(errorView);

		EventBus eventBus = clientFactory.getEventBus();

		eventBus.addHandler(ContentAreaChangeEvent.TYPE, new ContentAreaChangeEventHandler() {
			@Override public void onChange(ContentAreaChangeEvent event) {
				ContentPresenterType type = event.getContentPresenterType();
				IsWidget contentWidget = event.getContentWidget();

				navigatorPresenter.switchToArea(type);
				headerPresenter.switchToArea(type);
				footerPresenter.switchToArea(type);
				sectionInfoPresenter.switchToArea(type);

				final RootPanel contentArea = RootPanel.get("content_area");

				while (contentArea.getWidgetCount() > 0)
					contentArea.remove(0);

				if (contentWidget != null)
					contentArea.add(contentWidget);
			}
		});

		eventBus.addHandler(FatalErrorEvent.TYPE, new FatalErrorEventHandler() {
			@Override public void onFatalError(FatalErrorEvent event) {
				errorPresenter.showError(event.getTitle(), event.getMessage(), event.getThrowable());
			}
		});

		clientFactory.getHistoryManager().handleCurrentHistory();

		//Setting up history and display the current content

	}

	public static void setBrowserWindowTitle(String title) {
		if (Document.get() != null) {
			title = "sdb" + (title == null || title.isEmpty() ? "" : (" - " + title));
			Document.get().setTitle(title);
		}
	}

}

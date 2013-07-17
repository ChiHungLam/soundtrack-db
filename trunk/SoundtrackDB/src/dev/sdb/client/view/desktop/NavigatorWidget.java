package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.NavigatorView;

public class NavigatorWidget extends Composite implements NavigatorView {

	interface NavigatorWidgetUiBinder extends UiBinder<Widget, NavigatorWidget> {}
	private static NavigatorWidgetUiBinder uiBinder = GWT.create(NavigatorWidgetUiBinder.class);

	public interface NavigatorStyle extends CssResource {
		String highlighted();
		String contentLink();
	}

	@UiField protected NavigatorStyle style;

	@UiField Hyperlink homeLink;
	@UiField Hyperlink releaseLink;
	@UiField Hyperlink musicLink;
	@UiField Hyperlink soundtrackLink;
	@UiField Hyperlink seriesLink;

	@SuppressWarnings("unused") private Presenter presenter;

	public NavigatorWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		this.homeLink.setStyleName(this.style.contentLink());
		this.releaseLink.setStyleName(this.style.contentLink());
		this.musicLink.setStyleName(this.style.contentLink());
		this.soundtrackLink.setStyleName(this.style.contentLink());
		this.seriesLink.setStyleName(this.style.contentLink());
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dev.sdb.client.view.NavigatorView#highlightLink(dev.sdb.client.presenter.ContentPresenterType)
	 */
	@Override public void highlightLink(ContentPresenterType type) {
		//adding or removing the highlightes style depending on the given type
		this.homeLink.setStyleName(this.style.highlighted(), (type == ContentPresenterType.HOME));
		this.releaseLink.setStyleName(this.style.highlighted(), (type == ContentPresenterType.RELEASE));
		this.musicLink.setStyleName(this.style.highlighted(), (type == ContentPresenterType.MUSIC));
		this.soundtrackLink.setStyleName(this.style.highlighted(), (type == ContentPresenterType.SOUNDTRACK));
		this.seriesLink.setStyleName(this.style.highlighted(), (type == ContentPresenterType.SERIES));
	}
}

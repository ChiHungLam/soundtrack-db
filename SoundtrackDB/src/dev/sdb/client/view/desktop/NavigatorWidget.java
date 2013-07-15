package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.NavigatorView;

public class NavigatorWidget extends Composite implements NavigatorView {

	interface NavigatorWidgetUiBinder extends UiBinder<Widget, NavigatorWidget> {}
	private static NavigatorWidgetUiBinder uiBinder = GWT.create(NavigatorWidgetUiBinder.class);

	@SuppressWarnings("unused") private Presenter presenter;

	public NavigatorWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}

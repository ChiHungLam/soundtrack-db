package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NavigatorWidget extends Composite {

	private static NavigatorWidgetUiBinder uiBinder = GWT.create(NavigatorWidgetUiBinder.class);

	interface NavigatorWidgetUiBinder extends UiBinder<Widget, NavigatorWidget> {}

	public NavigatorWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

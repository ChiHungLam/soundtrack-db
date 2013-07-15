package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.HomeView;

public class HomeWidget extends Composite implements HomeView {

	interface HomeWidgetUiBinder extends UiBinder<Widget, HomeWidget> {}
	private static HomeWidgetUiBinder uiBinder = GWT.create(HomeWidgetUiBinder.class);

	@SuppressWarnings("unused") private Presenter presenter;

	public HomeWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}

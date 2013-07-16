package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.HeaderView;

public class HeaderWidget extends Composite implements HeaderView {

	interface HeaderWidgetUiBinder extends UiBinder<Widget, HeaderWidget> {}
	private static HeaderWidgetUiBinder uiBinder = GWT.create(HeaderWidgetUiBinder.class);

	@SuppressWarnings("unused") private Presenter presenter;

	public HeaderWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override public void setAreaInfo(ContentPresenterType type) {
		// TODO Auto-generated method stub

	}

}

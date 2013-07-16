package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.FooterView;

public class FooterWidget extends Composite implements FooterView {

	interface FooterWidgetUiBinder extends UiBinder<Widget, FooterWidget> {}
	private static FooterWidgetUiBinder uiBinder = GWT.create(FooterWidgetUiBinder.class);

	@SuppressWarnings("unused") private Presenter presenter;

	public FooterWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override public void highlightLink(ContentPresenterType type) {
		// TODO Auto-generated method stub

	}

}

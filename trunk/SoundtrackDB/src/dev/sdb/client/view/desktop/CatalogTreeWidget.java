package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.CatalogTreeView;

public class CatalogTreeWidget extends Composite implements CatalogTreeView {

	interface CatalogTreeWidgetUiBinder extends UiBinder<Widget, CatalogTreeWidget> {}
	private static CatalogTreeWidgetUiBinder uiBinder = GWT.create(CatalogTreeWidgetUiBinder.class);

	@SuppressWarnings("unused") private Presenter presenter;

	public CatalogTreeWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}

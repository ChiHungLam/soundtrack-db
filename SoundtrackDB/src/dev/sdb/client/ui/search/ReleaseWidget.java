package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ReleaseWidget extends Composite {

	interface ReleaseWidgetUiBinder extends UiBinder<Widget, ReleaseWidget> {}
	private static ReleaseWidgetUiBinder uiBinder = GWT.create(ReleaseWidgetUiBinder.class);

	@UiField SearchField searchField;
	@UiField ReleaseResultField resultField;

	public ReleaseWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SearchField getSearchField() {
		return this.searchField;
	}

	public ReleaseResultField getResultField() {
		return this.resultField;
	}
}

package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ReleaseQueryWidget extends AbstractQueryWidget {

	interface ReleaseWidgetUiBinder extends UiBinder<Widget, ReleaseQueryWidget> {}
	private static ReleaseWidgetUiBinder uiBinder = GWT.create(ReleaseWidgetUiBinder.class);

	@UiField SearchField searchField;
	@UiField ReleaseResultField resultField;

	public ReleaseQueryWidget() {
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

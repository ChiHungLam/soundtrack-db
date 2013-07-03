package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class QueryWidget extends Composite {

	interface QueryWidgetUiBinder extends UiBinder<Widget, QueryWidget> {}
	private static QueryWidgetUiBinder uiBinder = GWT.create(QueryWidgetUiBinder.class);

	@UiField SearchField searchField;
	@UiField QueryResultField resultField;

	public QueryWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SearchField getSearchField() {
		return this.searchField;
	}

	public QueryResultField getResultField() {
		return this.resultField;
	}

}

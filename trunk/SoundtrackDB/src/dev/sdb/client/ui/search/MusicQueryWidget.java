package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class MusicQueryWidget extends AbstractQueryWidget {

	interface MusicWidgetUiBinder extends UiBinder<Widget, MusicQueryWidget> {}
	private static MusicWidgetUiBinder uiBinder = GWT.create(MusicWidgetUiBinder.class);

	@UiField SearchField searchField;
	@UiField MusicResultField resultField;

	public MusicQueryWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SearchField getSearchField() {
		return this.searchField;
	}

	public MusicResultField getResultField() {
		return this.resultField;
	}
}

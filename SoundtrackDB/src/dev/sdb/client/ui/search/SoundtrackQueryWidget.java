package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class SoundtrackQueryWidget extends AbstractQueryWidget {

	interface SoundtrackQueryWidgetUiBinder extends UiBinder<Widget, SoundtrackQueryWidget> {}
	private static SoundtrackQueryWidgetUiBinder uiBinder = GWT.create(SoundtrackQueryWidgetUiBinder.class);

	@UiField SearchField searchField;
	@UiField SoundtrackResultField resultField;

	public SoundtrackQueryWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SearchField getSearchField() {
		return this.searchField;
	}

	public SoundtrackResultField getResultField() {
		return this.resultField;
	}
}

package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.MusicSearchView;

public class MusicSearchWidget extends Composite implements MusicSearchView {

	interface MusicSearchWidgetUiBinder extends UiBinder<Widget, MusicSearchWidget> {}
	private static MusicSearchWidgetUiBinder uiBinder = GWT.create(MusicSearchWidgetUiBinder.class);

	private Presenter presenter;

	public MusicSearchWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}

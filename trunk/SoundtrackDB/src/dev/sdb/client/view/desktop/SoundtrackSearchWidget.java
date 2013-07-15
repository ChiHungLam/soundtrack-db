package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.SoundtrackSearchView;

public class SoundtrackSearchWidget extends Composite implements SoundtrackSearchView {

	interface SoundtrackSearchWidgetUiBinder extends UiBinder<Widget, SoundtrackSearchWidget> {}
	private static SoundtrackSearchWidgetUiBinder uiBinder = GWT.create(SoundtrackSearchWidgetUiBinder.class);

	private Presenter presenter;

	public SoundtrackSearchWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}

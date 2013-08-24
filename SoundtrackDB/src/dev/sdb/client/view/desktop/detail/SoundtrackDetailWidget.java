package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.SoundtrackDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.SoundtrackMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackDetailWidget extends DetailWidget implements SoundtrackDetailView {

	interface SoundtrackDetailWidgetUiBinder extends UiBinder<Widget, SoundtrackDetailWidget> {}
	private static SoundtrackDetailWidgetUiBinder uiBinder = GWT.create(SoundtrackDetailWidgetUiBinder.class);

	@UiField(provided = true) MasterDataWidget masterData;

	public SoundtrackDetailWidget() {
		super();
		this.masterData = GWT.create(SoundtrackMasterData.class);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected SublistWidget getSublist() {
		return null;// soundtrack detail has no sublist.
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.masterData;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.masterData.initEntity(null);
		} else {
			Soundtrack soundtrack = (Soundtrack) entity;
			this.masterData.initEntity(soundtrack);
		}
	}

	@Override public void setPresenter(SoundtrackDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected SoundtrackDetailView.Presenter getPresenter() {
		return (SoundtrackDetailView.Presenter) super.getPresenter();
	}

}

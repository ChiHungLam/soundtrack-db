package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SoundtrackDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.MusicMasterData;
import dev.sdb.client.view.desktop.detail.master.ReleaseMasterData;
import dev.sdb.client.view.desktop.detail.master.SoundtrackMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackDetailWidget extends DetailWidget implements SoundtrackDetailView {

	interface SoundtrackDetailWidgetUiBinder extends UiBinder<Widget, SoundtrackDetailWidget> {}
	private static SoundtrackDetailWidgetUiBinder uiBinder = GWT.create(SoundtrackDetailWidgetUiBinder.class);

	@UiField SoundtrackMasterData soundtrackMasterData;
	@UiField ReleaseMasterData releaseMasterData;
	@UiField MusicMasterData musicMasterData;

	public SoundtrackDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected void addSublistColumns(DataGrid<Entity> table) {
		// soundtrack detail has no sublist.
	}

	@Override protected Entity getSublistEntity(Entity entity) {
		return null;// soundtrack detail has no sublist.
	}

	@Override protected ContentPresenterType getSublistContentPresenterType() {
		return null;// soundtrack detail has no sublist.
	}

	@Override protected SublistWidget getSublist() {
		return null;// soundtrack detail has no sublist.
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.soundtrackMasterData;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.soundtrackMasterData.initEntity(null);
			this.releaseMasterData.initEntity(null);
			this.musicMasterData.initEntity(null);
		} else {
			Soundtrack soundtrack = (Soundtrack) entity;
			this.soundtrackMasterData.initEntity(soundtrack);
			this.releaseMasterData.initEntity(soundtrack.getRelease());
			this.musicMasterData.initEntity(soundtrack.getMusic());
		}
	}

	@Override public void setPresenter(SoundtrackDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected SoundtrackDetailView.Presenter getPresenter() {
		return (SoundtrackDetailView.Presenter) super.getPresenter();
	}

}

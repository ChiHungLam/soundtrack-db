package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.MusicDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.MusicMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicDetailWidget extends DetailWidget implements MusicDetailView {

	interface MusicDetailWidgetUiBinder extends UiBinder<Widget, MusicDetailWidget> {}
	private static MusicDetailWidgetUiBinder uiBinder = GWT.create(MusicDetailWidgetUiBinder.class);

	@UiField(provided = true) MasterDataWidget masterData;
	@UiField SublistWidget sublist;

	public MusicDetailWidget() {
		super();
		this.masterData = GWT.create(MusicMasterData.class);
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.masterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.sublist;
	}

	@Override public void setPresenter(MusicDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected MusicDetailView.Presenter getPresenter() {
		return (MusicDetailView.Presenter) super.getPresenter();
	}

	@Override public void initEntity(Entity entity) {
		if (entity == null) {
			this.masterData.initEntity(null);
			this.sublist.setElementVisibility(-1);
		} else {
			Music music = (Music) entity;
			this.masterData.initEntity(music);
			getPresenter().getMusicReleaseListFromServer(this);
		}
	}

}

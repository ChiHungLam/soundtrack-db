package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.MusicController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.client.ui.detail.master.MusicMasterData;
import dev.sdb.client.ui.detail.sublist.ReleaseList;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicDetailWidget extends DetailWidget {

	interface MusicDetailWidgetUiBinder extends UiBinder<Widget, MusicDetailWidget> {}
	private static MusicDetailWidgetUiBinder uiBinder = GWT.create(MusicDetailWidgetUiBinder.class);

	@UiField MusicMasterData musicMasterData;
	@UiField ReleaseList releaseList;

	public MusicDetailWidget(MusicController controller) {
		super(controller);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.musicMasterData;
	}

	public ReleaseList getReleaseList() {
		return this.releaseList;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.musicMasterData.initEntity(null);
			this.releaseList.setElementVisibility(-1);
		} else {
			Music music = (Music) entity;
			this.musicMasterData.initEntity(music);
			((MusicController) getController()).getMusicReleaseListFromServer(this);
		}
	}
}

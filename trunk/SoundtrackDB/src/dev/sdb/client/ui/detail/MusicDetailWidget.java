package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.MusicController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.client.ui.detail.master.MusicMasterData;
import dev.sdb.client.ui.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicDetailWidget extends DetailWidget {

	interface MusicDetailWidgetUiBinder extends UiBinder<Widget, MusicDetailWidget> {}
	private static MusicDetailWidgetUiBinder uiBinder = GWT.create(MusicDetailWidgetUiBinder.class);

	@UiField MusicMasterData musicMasterData;
	@UiField SublistWidget musicReleaseList;

	public MusicDetailWidget(MusicController controller) {
		super(controller);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.musicMasterData;
	}

	public SublistWidget getSublist() {
		return this.musicReleaseList;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.musicMasterData.initEntity(null);
			this.musicReleaseList.setElementVisibility(-1);
		} else {
			Music music = (Music) entity;
			this.musicMasterData.initEntity(music);
			((MusicController) getController()).getMusicReleaseListFromServer(this);
		}
	}
}

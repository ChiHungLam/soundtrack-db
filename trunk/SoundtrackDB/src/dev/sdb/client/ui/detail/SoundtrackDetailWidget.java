package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.SoundtrackController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.client.ui.detail.master.MusicMasterData;
import dev.sdb.client.ui.detail.master.ReleaseMasterData;
import dev.sdb.client.ui.detail.master.SoundtrackMasterData;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackDetailWidget extends DetailWidget {

	interface SoundtrackDetailWidgetUiBinder extends UiBinder<Widget, SoundtrackDetailWidget> {}
	private static SoundtrackDetailWidgetUiBinder uiBinder = GWT.create(SoundtrackDetailWidgetUiBinder.class);

	@UiField SoundtrackMasterData soundtrackMasterData;
	@UiField ReleaseMasterData releaseMasterData;
	@UiField MusicMasterData musicMasterData;

	public SoundtrackDetailWidget(SoundtrackController controller) {
		super(controller);
		initWidget(uiBinder.createAndBindUi(this));
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
}

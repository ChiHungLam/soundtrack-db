package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackMasterData extends MasterDataWidget {

	interface SoundtrackMasterDataUiBinder extends UiBinder<Widget, SoundtrackMasterData> {}
	private static SoundtrackMasterDataUiBinder uiBinder = GWT.create(SoundtrackMasterDataUiBinder.class);

	@UiField LongBox idField;
	@UiField ReleaseMasterData releaseMasterData;
	@UiField MusicMasterData musicMasterData;

	public SoundtrackMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.idField.setValue(null);
			this.releaseMasterData.initEntity(null);
			this.musicMasterData.initEntity(null);
		} else {
			Soundtrack soundtrack = (Soundtrack) entity;
			this.idField.setValue(Long.valueOf(soundtrack.getId()));
			this.releaseMasterData.initEntity(soundtrack.getRelease());
			this.musicMasterData.initEntity(soundtrack.getMusic());
		}
	}
}

package dev.sdb.client.ui.detail.master;

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

	public SoundtrackMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		setCurrentEntity(entity);
		if (entity == null) {
			this.idField.setValue(null);
		} else {
			Soundtrack soundtrack = (Soundtrack) entity;
			this.idField.setValue(Long.valueOf(soundtrack.getId()));
		}
	}
}
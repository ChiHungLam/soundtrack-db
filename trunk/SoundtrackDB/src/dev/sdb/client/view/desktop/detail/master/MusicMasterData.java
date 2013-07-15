package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicMasterData extends MasterDataWidget {

	interface MusicMasterDataUiBinder extends UiBinder<Widget, MusicMasterData> {}
	private static MusicMasterDataUiBinder uiBinder = GWT.create(MusicMasterDataUiBinder.class);

	@UiField LongBox idField;
	@UiField TextBox titleField;

	public MusicMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.idField.setValue(null);
			this.titleField.setText(null);
		} else {
			Music music = (Music) entity;
			this.idField.setValue(Long.valueOf(music.getId()));
			this.titleField.setText(music.getTitle());
		}
	}
}

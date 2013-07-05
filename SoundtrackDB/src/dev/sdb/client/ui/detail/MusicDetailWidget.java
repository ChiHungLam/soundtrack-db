package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.AbstractSearchController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.client.ui.detail.master.MusicMasterData;
import dev.sdb.shared.model.entity.Entity;

public class MusicDetailWidget extends DetailWidget {

	interface MusicDetailWidgetUiBinder extends UiBinder<Widget, MusicDetailWidget> {}
	private static MusicDetailWidgetUiBinder uiBinder = GWT.create(MusicDetailWidgetUiBinder.class);

	@UiField MusicMasterData musicMasterData;

	public MusicDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.musicMasterData;
	}

	public void initEntity(Entity entity, AbstractSearchController controller) {
		if (entity == null) {
			this.musicMasterData.initEntity(null);
		} else {
			this.musicMasterData.initEntity(entity);
		}
	}
}

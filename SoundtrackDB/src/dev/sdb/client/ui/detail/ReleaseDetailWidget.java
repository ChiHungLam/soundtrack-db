package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.ReleaseController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.client.ui.detail.master.ReleaseMasterData;
import dev.sdb.client.ui.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseDetailWidget extends DetailWidget {

	interface ReleaseDetailWidgetUiBinder extends UiBinder<Widget, ReleaseDetailWidget> {}
	private static ReleaseDetailWidgetUiBinder uiBinder = GWT.create(ReleaseDetailWidgetUiBinder.class);

	@UiField ReleaseMasterData releaseMasterData;
	@UiField SublistWidget releaseSoundtrackList;

	public ReleaseDetailWidget(ReleaseController controller) {
		super(controller);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.releaseMasterData;
	}

	public SublistWidget getSublist() {
		return this.releaseSoundtrackList;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.releaseMasterData.initEntity(null);
			this.releaseSoundtrackList.setElementVisibility(-1);
		} else {
			Release release = (Release) entity;
			this.releaseMasterData.initEntity(release);
			((ReleaseController) getController()).getSequenceListFromServer(this);
		}
	}
}

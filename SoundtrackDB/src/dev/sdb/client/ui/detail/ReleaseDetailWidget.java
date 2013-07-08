package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.controller.AbstractDataController;
import dev.sdb.client.controller.ReleaseController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.client.ui.detail.master.ReleaseMasterData;
import dev.sdb.client.ui.detail.sublist.SequenceList;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseDetailWidget extends DetailWidget {

	interface ReleaseDetailWidgetUiBinder extends UiBinder<Widget, ReleaseDetailWidget> {}
	private static ReleaseDetailWidgetUiBinder uiBinder = GWT.create(ReleaseDetailWidgetUiBinder.class);

	@UiField ReleaseMasterData releaseMasterData;
	@UiField SequenceList sequenceList;

	public ReleaseDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.releaseMasterData;
	}

	public SequenceList getSequenceList() {
		return this.sequenceList;
	}

	public void initEntity(Entity entity, AbstractDataController controller) {
		this.sequenceList.clearTable();

		if (entity == null) {
			this.releaseMasterData.initEntity(null);
		} else {
			Release release = (Release) entity;
			this.releaseMasterData.initEntity(release);
			((ReleaseController) controller).getSequenceListFromServer(this);
		}
	}
}

package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.ReleaseDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.ReleaseMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseDetailWidget extends DetailWidget implements ReleaseDetailView {

	interface ReleaseDetailWidgetUiBinder extends UiBinder<Widget, ReleaseDetailWidget> {}
	private static ReleaseDetailWidgetUiBinder uiBinder = GWT.create(ReleaseDetailWidgetUiBinder.class);

	@UiField ReleaseMasterData releaseMasterData;
	@UiField SublistWidget releaseSoundtrackList;

	public ReleaseDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
	}

	@Override public DataGrid<Entity> getSublistTable() {
		return this.releaseSoundtrackList.getTable();
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.releaseMasterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.releaseSoundtrackList;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.releaseMasterData.initEntity(null);
			this.releaseSoundtrackList.setElementVisibility(-1);
		} else {
			Release release = (Release) entity;
			this.releaseMasterData.initEntity(release);
			getPresenter().getSequenceListFromServer(this);
		}
	}

	@Override public void setPresenter(ReleaseDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected ReleaseDetailView.Presenter getPresenter() {
		return (ReleaseDetailView.Presenter) super.getPresenter();
	}


}

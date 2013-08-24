package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.ReleaseDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.ReleaseMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseDetailWidget extends DetailWidget implements ReleaseDetailView {

	interface ReleaseDetailWidgetUiBinder extends UiBinder<Widget, ReleaseDetailWidget> {}
	private static ReleaseDetailWidgetUiBinder uiBinder = GWT.create(ReleaseDetailWidgetUiBinder.class);

	@UiField(provided = true) MasterDataWidget masterData;
	@UiField SublistWidget sublist;

	public ReleaseDetailWidget() {
		super();
		this.masterData = GWT.create(ReleaseMasterData.class);
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
	}

	@Override public void showSublistResult(String resultInfo, Result searchResult) {
		getSublistTable().setVisibleRange(0, searchResult.getTotalLength());
		super.showSublistResult(resultInfo, searchResult);
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.masterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.sublist;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.masterData.initEntity(null);
			this.sublist.setElementVisibility(-1);
		} else {
			Release release = (Release) entity;
			this.masterData.initEntity(release);
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

package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.SeriesDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.SeriesMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Series;

public class SeriesDetailWidget extends DetailWidget implements SeriesDetailView {

	interface SeriesDetailViewUiBinder extends UiBinder<Widget, SeriesDetailWidget> {}
	private static SeriesDetailViewUiBinder uiBinder = GWT.create(SeriesDetailViewUiBinder.class);

	@UiField(provided = true) MasterDataWidget masterData;
	@UiField SublistWidget sublist;

	public SeriesDetailWidget() {
		super();
		this.masterData = GWT.create(SeriesMasterData.class);
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
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
			Series series = (Series) entity;
			this.masterData.initEntity(series);
			getPresenter().getSeriesReleaseListFromServer(this);
		}
	}

	@Override public void setPresenter(SeriesDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected SeriesDetailView.Presenter getPresenter() {
		return (SeriesDetailView.Presenter) super.getPresenter();
	}
}

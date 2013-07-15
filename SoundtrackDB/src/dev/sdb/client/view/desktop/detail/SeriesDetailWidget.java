package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SeriesDetailView;
import dev.sdb.client.view.desktop.UiFactoryImpl;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.SeriesMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Series;

public class SeriesDetailWidget extends DetailWidget implements SeriesDetailView {

	interface SeriesDetailViewUiBinder extends UiBinder<Widget, SeriesDetailWidget> {}
	private static SeriesDetailViewUiBinder uiBinder = GWT.create(SeriesDetailViewUiBinder.class);

	@UiField SeriesMasterData seriesMasterData;
	@UiField SublistWidget seriesReleaseList;

	public SeriesDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		initSublist(this.seriesReleaseList.getTable());
	}

	@Override protected Entity getSublistEntity(Entity entity) {
		return entity;
	}

	@Override protected ContentPresenterType getSublistContentPresenterType() {
		return ContentPresenterType.RELEASE;
	}

	@Override protected void addSublistColumns(DataGrid<Entity> table) {
		UiFactoryImpl.addReleaseColumns(table, true, true);
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.seriesMasterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.seriesReleaseList;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.seriesMasterData.initEntity(null);
			this.seriesReleaseList.setElementVisibility(-1);
		} else {
			Series series = (Series) entity;
			this.seriesMasterData.initEntity(series);
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

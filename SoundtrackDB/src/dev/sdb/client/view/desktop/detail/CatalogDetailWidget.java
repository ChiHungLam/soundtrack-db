package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.CatalogDetailView;
import dev.sdb.client.view.desktop.detail.master.CatalogMasterData;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

public class CatalogDetailWidget extends DetailWidget implements CatalogDetailView {

	interface CatalogDetailWidgetUiBinder extends UiBinder<Widget, CatalogDetailWidget> {}
	private static CatalogDetailWidgetUiBinder uiBinder = GWT.create(CatalogDetailWidgetUiBinder.class);

	@UiField(provided = true) MasterDataWidget masterData;
	@UiField SublistWidget sublist;

	public CatalogDetailWidget() {
		super();
		this.masterData = GWT.create(CatalogMasterData.class);
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
	}

	@Override public void setPresenter(CatalogDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected CatalogDetailView.Presenter getPresenter() {
		return (CatalogDetailView.Presenter) super.getPresenter();
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.masterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.sublist;
	}

	@Override public void initEntity(Entity entity) {
		if (entity == null) {
			this.masterData.initEntity(null);
			this.sublist.setElementVisibility(-1);
		} else {
			Catalog catalog = (Catalog) entity;
			this.masterData.initEntity(catalog);
			getPresenter().getCatalogEntriesFromServer(this);
		}
	}

}

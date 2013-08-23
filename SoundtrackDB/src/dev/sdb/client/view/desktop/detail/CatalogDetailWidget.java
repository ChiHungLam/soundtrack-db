package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
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

	@UiField CatalogMasterData catalogMasterData;
	@UiField SublistWidget catalogEntryList;

	public CatalogDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
	}

	@Override public DataGrid<Entity> getSublistTable() {
		return this.catalogEntryList.getTable();
	}

	@Override public void setPresenter(CatalogDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected CatalogDetailView.Presenter getPresenter() {
		return (CatalogDetailView.Presenter) super.getPresenter();
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.catalogMasterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.catalogEntryList;
	}

	@Override public void initEntity(Entity entity) {
		if (entity == null) {
			this.catalogMasterData.initEntity(null);
			this.catalogEntryList.setElementVisibility(-1);
		} else {
			Catalog catalog = (Catalog) entity;
			this.catalogMasterData.initEntity(catalog);
			getPresenter().getCatalogEntriesFromServer(this);
		}
	}

}

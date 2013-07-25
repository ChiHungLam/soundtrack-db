package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

public class CatalogMasterData extends MasterDataWidget {

	interface CatalogMasterDataUiBinder extends UiBinder<Widget, CatalogMasterData> {}
	private static CatalogMasterDataUiBinder uiBinder = GWT.create(CatalogMasterDataUiBinder.class);

	@UiField LongBox idField;
	@UiField TextBox titleField;

	public CatalogMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.idField.setValue(null);
			this.titleField.setText(null);
		} else {
			Catalog catalog = (Catalog) entity;
			this.idField.setValue(Long.valueOf(catalog.getId()));
			this.titleField.setText(catalog.getTitle());
		}
	}

}

package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Series;

public class SeriesMasterData extends MasterDataWidget {

	interface SeriesMasterDataUiBinder extends UiBinder<Widget, SeriesMasterData> {}
	private static SeriesMasterDataUiBinder uiBinder = GWT.create(SeriesMasterDataUiBinder.class);

	@UiField LongBox idField;
	@UiField TextBox titleField;

	public SeriesMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.idField.setValue(null);
			this.titleField.setText(null);
		} else {
			Series series = (Series) entity;
			this.idField.setValue(Long.valueOf(series.getId()));
			this.titleField.setText(series.getTitle());
		}
	}
}

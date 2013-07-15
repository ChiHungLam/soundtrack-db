package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseMasterData extends MasterDataWidget {

	interface ReleaseMasterDataUiBinder extends UiBinder<Widget, ReleaseMasterData> {}
	private static ReleaseMasterDataUiBinder uiBinder = GWT.create(ReleaseMasterDataUiBinder.class);

	@UiField LongBox idField;
	@UiField TextBox titleField;

	public ReleaseMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		setCurrentEntity(entity);
		if (entity == null) {
			this.idField.setValue(null);
			this.titleField.setText("");
		} else {
			Release release = (Release) entity;
			this.idField.setValue(Long.valueOf(release.getId()));
			this.titleField.setText(release.getTitle());
		}
	}
}

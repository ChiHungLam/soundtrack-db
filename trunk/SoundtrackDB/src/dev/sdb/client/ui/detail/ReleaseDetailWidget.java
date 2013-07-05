package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseDetailWidget extends DetailWidget {

	interface ReleaseDetailWidgetUiBinder extends UiBinder<Widget, ReleaseDetailWidget> {}
	private static ReleaseDetailWidgetUiBinder uiBinder = GWT.create(ReleaseDetailWidgetUiBinder.class);

	@UiField LongBox idField;
	@UiField TextBox titleField;

	public ReleaseDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected LongBox getIdField() {
		return this.idField;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			setCurrentId(0);
			this.titleField.setText("");
		} else {
			Release release = (Release) entity;
			setCurrentId(release.getId());
			this.titleField.setText(release.getTitle());
		}
	}
}

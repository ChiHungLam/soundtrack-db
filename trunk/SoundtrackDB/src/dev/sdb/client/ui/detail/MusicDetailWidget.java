package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicDetailWidget extends DetailWidget {

	interface MusicDetailWidgetUiBinder extends UiBinder<Widget, MusicDetailWidget> {}
	private static MusicDetailWidgetUiBinder uiBinder = GWT.create(MusicDetailWidgetUiBinder.class);

	@UiField LongBox idField;
	@UiField TextBox titleField;

	public MusicDetailWidget() {
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
			Music music = (Music) entity;
			setCurrentId(music.getId());
			this.titleField.setText(music.getTitle());
		}
	}

}

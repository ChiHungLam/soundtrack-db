package dev.sdb.client.ui.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackDetailWidget extends DetailWidget {

	interface SoundtrackDetailWidgetUiBinder extends UiBinder<Widget, SoundtrackDetailWidget> {}
	private static SoundtrackDetailWidgetUiBinder uiBinder = GWT.create(SoundtrackDetailWidgetUiBinder.class);

	@UiField LongBox idField;
	@UiField ReleaseDetailWidget releaseDetail;
	@UiField MusicDetailWidget musicDetail;

	public SoundtrackDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override protected LongBox getIdField() {
		return this.idField;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			setCurrentId(0);
			this.releaseDetail.initEntity(null);
			this.musicDetail.initEntity(null);
		} else {
			Soundtrack soundtrack = (Soundtrack) entity;
			setCurrentId(soundtrack.getId());
			this.releaseDetail.initEntity(soundtrack.getRelease());
			this.musicDetail.initEntity(soundtrack.getMusic());
		}
	}
}

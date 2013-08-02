package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
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
	@UiField Image coverArtwork;

	public ReleaseMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.idField.setValue(null);
			this.titleField.setText("");
			this.coverArtwork.setVisible(false);
		} else {
			Release release = (Release) entity;
			String artworkUrl = URL.encode(release.getArtworkUrl());
			this.idField.setValue(Long.valueOf(release.getId()));
			this.titleField.setText(release.getTitle());
			this.coverArtwork.setUrl(artworkUrl);
			this.coverArtwork.setTitle(artworkUrl);
			this.coverArtwork.setAltText(artworkUrl);
			this.coverArtwork.setVisible(true);
		}
	}


}

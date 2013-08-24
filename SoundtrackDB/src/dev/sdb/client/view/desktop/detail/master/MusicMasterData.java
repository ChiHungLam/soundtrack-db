package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.UiFactory.HtmlFactory;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicMasterData extends MasterDataWidget {

	interface MusicMasterDataUiBinder extends UiBinder<Widget, MusicMasterData> {}
	private static MusicMasterDataUiBinder uiBinder = GWT.create(MusicMasterDataUiBinder.class);

	@UiField HTML content;

	public MusicMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.content.setHTML("");
		} else {
			Music music = (Music) entity;

			HtmlFactory htmlFactory = getPresenter().getHtmlFactory();
			SafeHtml html = htmlFactory.getMusicInfoDetailed(music);
			this.content.setHTML(html);
		}
	}
}

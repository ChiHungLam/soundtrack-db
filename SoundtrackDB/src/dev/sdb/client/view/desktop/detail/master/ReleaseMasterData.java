package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.UiFactory.HtmlFactory;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseMasterData extends MasterDataWidget {

	interface ReleaseMasterDataUiBinder extends UiBinder<Widget, ReleaseMasterData> {}
	private static ReleaseMasterDataUiBinder uiBinder = GWT.create(ReleaseMasterDataUiBinder.class);

	@UiField HTML content;

	public ReleaseMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.content.setHTML("");
		} else {
			Release release = (Release) entity;

			HtmlFactory htmlFactory = getPresenter().getHtmlFactory();
			SafeHtml html = htmlFactory.getReleaseInfoDetailed(release);
			this.content.setHTML(html);
		}
	}


}

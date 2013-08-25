package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.UiFactory.HtmlFactory;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackMasterData extends MasterDataWidget {

	interface SoundtrackMasterDataUiBinder extends UiBinder<Widget, SoundtrackMasterData> {}
	private static SoundtrackMasterDataUiBinder uiBinder = GWT.create(SoundtrackMasterDataUiBinder.class);

	@UiField HTML content;

	public SoundtrackMasterData() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.content.setHTML("");
		} else {
			Soundtrack soundtrack = (Soundtrack) entity;

			HtmlFactory htmlFactory = getPresenter().getHtmlFactory();

			SafeHtml releaseHtml = htmlFactory.getReleaseInfoDetailed(soundtrack.getRelease());
			SafeHtml seqNumHtml = htmlFactory.getSoundtrackSeqNum(soundtrack);
			SafeHtml musicHtml = htmlFactory.getMusicInfoCompact(soundtrack.getMusic());
			SafeHtml timeHtml = htmlFactory.getSoundtrackTime(soundtrack);
			
			String html = "<div style='text-align:left;'>";
			html += releaseHtml.asString() + "<br><br>";
			html += "<table><tr><td style='position:relative;width:100px;'>";
			html += seqNumHtml.asString();
			html += "</td><td>";
			html += musicHtml.asString();
			html += "</td></tr></table>";
			html += "<br><br>";
			html += "Zeitindex: " + timeHtml.asString();
			html += "</div>";

			this.content.setHTML(html);
		}
	}
}

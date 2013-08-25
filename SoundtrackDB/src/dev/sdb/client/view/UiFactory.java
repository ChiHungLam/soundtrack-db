package dev.sdb.client.view;

import com.google.gwt.safehtml.shared.SafeHtml;

import dev.sdb.client.ClientFactory;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public interface UiFactory {

	public interface HtmlFactory {
		SafeHtml getReleaseInfoCompact(Release release);
		SafeHtml getReleaseInfoDetailed(Release release);

		SafeHtml getMusicInfoCompact(Music music);
		SafeHtml getMusicInfoDetailed(Music music);

		SafeHtml getSeriesInfo(Series series);

		SafeHtml getSoundtrackSeqNum(Soundtrack soundtrack);

		SafeHtml getSoundtrackTime(Soundtrack soundtrack);
	}

	void setClientFactory(ClientFactory clientFactory);

	HtmlFactory getHtmlFactory();

	NavigatorView getNavigatorView();
	HeaderView getHeaderView();
	FooterView getFooterView();
	SectionInfoView getSectionInfoView();

	ErrorView getErrorView();

	HomeView getHomeView();

	ReleaseQueryView getReleaseQueryView();
	MusicQueryView getMusicQueryView();
	SoundtrackQueryView getSoundtrackQueryView();
	SeriesQueryView getSeriesQueryView();
	CatalogBrowseView getCatalogTreeView(CatalogBrowseView.Presenter presenter);

	ReleaseDetailView getReleaseDetailView();
	MusicDetailView getMusicDetailView();
	SoundtrackDetailView getSoundtrackDetailView();
	SeriesDetailView getSeriesDetailView();
	CatalogDetailView getCatalogDetailView();
	
}

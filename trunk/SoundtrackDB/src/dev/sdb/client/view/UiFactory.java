package dev.sdb.client.view;

public interface UiFactory {

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
	CatalogTreeView getCatalogTreeView(CatalogTreeView.Presenter presenter);

	ReleaseDetailView getReleaseDetailView();
	MusicDetailView getMusicDetailView();
	SoundtrackDetailView getSoundtrackDetailView();
	SeriesDetailView getSeriesDetailView();
	CatalogDetailView getCatalogDetailView();

}

package dev.sdb.client.view;

public interface UiFactory {

	NavigatorView getNavigatorView();
	HeaderView getHeaderView();
	FooterView getFooterView();

	ErrorView getErrorView();

	HomeView getHomeView();

	ReleaseQueryView getReleaseQueryView();
	MusicQueryView getMusicQueryView();
	SoundtrackQueryView getSoundtrackQueryView();
	SeriesQueryView getSeriesQueryView();

	ReleaseDetailView getReleaseDetailView();
	MusicDetailView getMusicDetailView();
	SoundtrackDetailView getSoundtrackDetailView();
	SeriesDetailView getSeriesDetailView();

}

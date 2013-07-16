package dev.sdb.client.view;

import com.google.gwt.user.client.ui.HasEnabled;


public interface UiFactory {
	NavigatorView getNavigatorView();
	HeaderView getHeaderView();
	FooterView getFooterView();

	HomeView getHomeView();

	ReleaseQueryView getReleaseQueryView();
	MusicQueryView getMusicQueryView();
	SoundtrackQueryView getSoundtrackQueryView();
	SeriesQueryView getSeriesQueryView();

	ReleaseDetailView getReleaseDetailView();
	MusicDetailView getMusicDetailView();
	SoundtrackDetailView getSoundtrackDetailView();
	SeriesDetailView getSeriesDetailView();

	void showRpcError(Throwable caught, String msg, final HasEnabled hasEnabled);

}

package dev.sdb.client.view.desktop;

import dev.sdb.client.view.HomeView;
import dev.sdb.client.view.MusicSearchView;
import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.ReleaseSearchView;
import dev.sdb.client.view.SoundtrackSearchView;
import dev.sdb.client.view.UiFactory;
import dev.sdb.client.view.desktop.detail.MusicDetailWidget;
import dev.sdb.client.view.desktop.detail.ReleaseDetailWidget;
import dev.sdb.client.view.desktop.detail.SoundtrackDetailWidget;

public class UiFactoryImpl implements UiFactory {

	
	private static final NavigatorView navigatorView = new NavigatorWidget();

	private static HomeView homeView;

	private static ReleaseSearchView releaseSearchView;
	private static MusicSearchView musicSearchView;
	private static SoundtrackSearchView soundtrackSearchView;

	private static ReleaseDetailWidget releaseDetailView;
	private static MusicDetailWidget musicDetailView;
	private static SoundtrackDetailWidget soundtrackDetailView;

	
	@Override public NavigatorView getNavigatorView() {
		return navigatorView;
	}

	@Override public HomeView getHomeView() {
		if (homeView == null)
			homeView = new HomeWidget();
		return homeView;
	}

	@Override public ReleaseSearchView getReleaseSearchView() {
		if (releaseSearchView == null)
			releaseSearchView = new ReleaseSearchWidget();
		return releaseSearchView;
	}

	@Override public MusicSearchView getMusicSearchView() {
		if (musicSearchView == null)
			musicSearchView = new MusicSearchWidget();
		return musicSearchView;
	}

	@Override public SoundtrackSearchView getSoundtrackSearchView() {
		if (soundtrackSearchView == null)
			soundtrackSearchView = new SoundtrackSearchWidget();
		return soundtrackSearchView;
	}

	@Override public ReleaseDetailWidget getReleaseDetailView() {
		if (releaseDetailView == null)
			releaseDetailView = new ReleaseDetailWidget();
		return releaseDetailView;
	}

	@Override public MusicDetailWidget getMusicDetailView() {
		if (musicDetailView == null)
			musicDetailView = new MusicDetailWidget();
		return musicDetailView;
	}

	@Override public SoundtrackDetailWidget getSoundtrackDetailView() {
		if (soundtrackDetailView == null)
			soundtrackDetailView = new SoundtrackDetailWidget();
		return soundtrackDetailView;
	}

}

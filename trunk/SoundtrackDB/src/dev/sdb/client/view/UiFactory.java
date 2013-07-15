package dev.sdb.client.view;


public interface UiFactory {

	NavigatorView getNavigatorView();

	HomeView getHomeView();

	ReleaseSearchView getReleaseSearchView();
	MusicSearchView getMusicSearchView();
	SoundtrackSearchView getSoundtrackSearchView();
}

package dev.sdb.client.view;

import dev.sdb.client.view.desktop.detail.MusicDetailWidget;
import dev.sdb.client.view.desktop.detail.ReleaseDetailWidget;
import dev.sdb.client.view.desktop.detail.SoundtrackDetailWidget;

public interface UiFactory {

	NavigatorView getNavigatorView();

	HomeView getHomeView();

	ReleaseSearchView getReleaseSearchView();

	MusicSearchView getMusicSearchView();

	SoundtrackSearchView getSoundtrackSearchView();

	ReleaseDetailWidget getReleaseDetailView();

	MusicDetailWidget getMusicDetailView();

	SoundtrackDetailWidget getSoundtrackDetailView();
}

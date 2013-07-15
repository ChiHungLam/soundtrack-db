package dev.sdb.client.view;

import com.google.gwt.user.client.ui.HasEnabled;


public interface UiFactory {

	NavigatorView getNavigatorView();

	HomeView getHomeView();

	ReleaseQueryView getReleaseQueryView();
	MusicQueryView getMusicQueryView();
	SoundtrackQueryView getSoundtrackQueryView();

	ReleaseDetailView getReleaseDetailView();
	MusicDetailView getMusicDetailView();
	SoundtrackDetailView getSoundtrackDetailView();

	void showRpcError(Throwable caught, String msg, final HasEnabled hasEnabled);
}

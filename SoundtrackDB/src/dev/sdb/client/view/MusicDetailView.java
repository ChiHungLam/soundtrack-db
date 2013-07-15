package dev.sdb.client.view;

import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;

public interface MusicDetailView extends DetailView {

	public interface Presenter {
		void getMusicReleaseListFromServer(MusicDetailView view);
	}

	void setPresenter(Presenter presenter);

	SublistWidget getSublist();
}

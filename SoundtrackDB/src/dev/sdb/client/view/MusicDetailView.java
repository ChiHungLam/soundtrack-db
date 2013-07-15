package dev.sdb.client.view;


public interface MusicDetailView extends DetailView {

	public interface Presenter extends DetailView.Presenter {
		void getMusicReleaseListFromServer(MusicDetailView view);
	}

	void setPresenter(Presenter presenter);
}

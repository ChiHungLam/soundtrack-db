package dev.sdb.client.view;


public interface SeriesDetailView extends DetailView {

	public interface Presenter extends DetailView.Presenter {
		void getSeriesReleaseListFromServer(SeriesDetailView view);
	}

	void setPresenter(Presenter presenter);
}

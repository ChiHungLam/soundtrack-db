package dev.sdb.client.view;


public interface ReleaseDetailView extends DetailView {

	public interface Presenter extends DetailView.Presenter {
		void getSequenceListFromServer(ReleaseDetailView view);
	}

	void setPresenter(Presenter presenter);

}

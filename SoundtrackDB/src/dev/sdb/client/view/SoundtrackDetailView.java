package dev.sdb.client.view;


public interface SoundtrackDetailView extends DetailView {

	public interface Presenter extends DetailView.Presenter {}

	void setPresenter(Presenter presenter);
}

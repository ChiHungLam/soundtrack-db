package dev.sdb.client.view;


public interface SoundtrackQueryView extends QueryView {

	public interface Presenter extends QueryView.Presenter {}

	void setPresenter(Presenter presenter);
}

package dev.sdb.client.view;


public interface ReleaseQueryView extends QueryView {

	public interface Presenter extends QueryView.Presenter {}

	void setPresenter(Presenter presenter);


}

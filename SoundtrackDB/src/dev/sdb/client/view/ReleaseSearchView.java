package dev.sdb.client.view;


public interface ReleaseSearchView extends SearchView {

	public interface Presenter extends SearchView.Presenter {}

	void setPresenter(Presenter presenter);


}

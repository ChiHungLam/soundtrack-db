package dev.sdb.client.view;


public interface SeriesQueryView extends QueryView {

	public interface Presenter extends QueryView.Presenter {}

	void setPresenter(Presenter presenter);
}

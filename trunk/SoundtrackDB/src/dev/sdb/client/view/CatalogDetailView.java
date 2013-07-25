package dev.sdb.client.view;

public interface CatalogDetailView extends DetailView {

	public interface Presenter extends DetailView.Presenter {
		void getCatalogEntriesFromServer(CatalogDetailView view);
	}

	void setPresenter(Presenter presenter);

}

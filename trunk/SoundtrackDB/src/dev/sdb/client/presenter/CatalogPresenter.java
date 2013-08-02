package dev.sdb.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.CatalogDetailView;
import dev.sdb.client.view.CatalogTreeView;
import dev.sdb.client.view.DetailView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

public class CatalogPresenter extends AbstractBrowsePresenter implements CatalogTreeView.Presenter,
		CatalogDetailView.Presenter {

	private CatalogTreeView treeView;

	public CatalogPresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.CATALOG, Flavor.CATALOG);
	}

	@Override public void getCatalogEntriesFromServer(CatalogDetailView view) {
		// TODO Auto-generated method stub

	}

	@Override public void getCatalogReleases(final Catalog catalog, Range range, final CatalogTreeView view) {
		SearchServiceAsync service = getClientFactory().getSearchService();

		// Then, we send the input to the server.
		service.getCatalogReleaseList(catalog.getId(), range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				view.showResult(searchResult);
			}

			public void onFailure(Throwable caught) {
				handleRpcError("[" + Flavor.CATALOG.name() + "] getting releases of " + catalog.getMatch(), caught);
			}
		});
	}

	@Override public void getCatalogLevelEntries(final long parentId, final AsyncDataProvider<Entity> dataProvider) {
		SearchServiceAsync service = getClientFactory().getSearchService();

		// Then, we send the input to the server.
		service.getCatalogList(parentId, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				//				addHistorySearch(term);
				dataProvider.updateRowData(0, searchResult.getResultChunk());
			}

			public void onFailure(Throwable caught) {
				handleRpcError("[" + Flavor.CATALOG.name() + "] getting children of " + parentId, caught);
			}
		});
	}

	@Override public IsWidget getView(String state) {
		assert (state != null);

		if (state.isEmpty())
			return getTreeView("");

		if (state.startsWith("parent=")) {
			return getTreeView(getParentFromState(state));

		} else if (state.startsWith("id=")) {
			long id = getIdFromState(state);
			if (id > 0) {
				return getDetailView(id);
			}
		}
		return getTreeView("");
	}

	private String getParentFromState(String state) {
		// TODO Auto-generated method stub
		return "";
	}

	private IsWidget getTreeView(String term) {
		assert (term != null);

		String title = getType().getName() + "-Suche" + ((term.isEmpty()) ? "" : (": " + term));
		SoundtrackDB.setBrowserWindowTitle(title);

		if (this.treeView == null) {
			this.treeView = createTreeView(term);
			//			if (!term.isEmpty()) {
			//				this.lastSearchTerm = term;
			//				getSearchFromServer(this.treeView);
			//			}
		} else {
			//			if (!this.treeView.getText().equalsIgnoreCase(term)) {
			//				this.treeView.setText(term);
			//				this.lastSearchTerm = term;
			//
			//				if (term.isEmpty()) {
			//					this.treeView.reset();
			//				} else {
			//					getSearchFromServer(this.treeView);
			//				}
			//			}
		}
		return this.treeView;
	}

	protected CatalogTreeView createTreeView(String term) {
		// Create the query widget instance
		final CatalogTreeView treeView = getClientFactory().getUi().getCatalogTreeView(this);

		// Set the search term
		//		treeView.setText(term);
		//
		//		// Create a data provider.
		//		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
		//			@Override protected void onRangeChanged(HasData<Entity> display) {
		//				getSearchFromServer(treeView);
		//			}
		//		};
		//
		//		treeView.setDataProvider(dataProvider);

		return treeView;
	}

	@Override protected DetailView createDetailView() {
		final CatalogDetailView view = getClientFactory().getUi().getCatalogDetailView();
		view.setPresenter(this);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getCatalogEntriesFromServer(view);
			}
		};

		view.setSublistDataProvider(dataProvider);

		return view;
	}

}

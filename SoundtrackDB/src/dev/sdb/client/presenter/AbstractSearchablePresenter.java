package dev.sdb.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.QueryView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;

public abstract class AbstractSearchablePresenter extends AbstractBrowsePresenter implements QueryView.Presenter {

	private String lastSearchTerm;
	private QueryView queryView;

	public AbstractSearchablePresenter(ClientFactory clientFactory, ContentPresenterType type, Flavor flavor) {
		super(clientFactory, type, flavor);
	}

	@Override public void onSearch(String term) {
		setLastSearchTerm(term);
		getSearchFromServer(this.queryView);
	}

	protected abstract QueryView createQueryView(String term);

	public IsWidget getView(String state) {
		assert (state != null);

		if (state.isEmpty())
			return getQueryView("");

		if (state.startsWith("search=")) {
			return getQueryView(getSearchFromState(state));

		} else if (state.startsWith("id=")) {
			long id = getIdFromState(state);
			if (id > 0) {
				return getDetailView(id);
			}
		}
		return getQueryView("");
	}

	protected String getSearchToken(String term) {
		return getType().getToken() + "?search=" + term;
	}

	private String getSearchFromState(String state) {
		try {
			return state.substring(7);
		} catch (Exception e) {
			return "";
		}
	}

	private IsWidget getQueryView(String term) {
		assert (term != null);

		String title = getType().getName() + "-Suche" + ((term.isEmpty()) ? "" : (": " + term));
		SoundtrackDB.setBrowserWindowTitle(title);

		if (this.queryView == null) {
			this.queryView = createQueryView(term);
			if (!term.isEmpty()) {
				this.lastSearchTerm = term;
				getSearchFromServer(this.queryView);
			}
		} else {
			if (!this.queryView.getText().equalsIgnoreCase(term)) {
				this.queryView.setText(term);
				this.lastSearchTerm = term;

				if (term.isEmpty()) {
					this.queryView.reset();
				} else {
					getSearchFromServer(this.queryView);
				}
			}
		}
		return this.queryView;
	}

	protected void getSearchFromServer(final QueryView view) {

		//if there hasn't been a search before, cancel the action
		if (this.lastSearchTerm == null) {
			view.reset();
			return;
		}

		view.lockForSearch();

		final Range range = view.getRange();
		final boolean ascending = view.isSortAscending();

		final String term = this.lastSearchTerm;

		SearchServiceAsync service = getClientFactory().getSearchService();

		final Flavor flavor = getFlavor();

		// Then, we send the input to the server.
		service.search(flavor, term, range, ascending, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				addHistorySearch(term);

				String title = getType().getName() + "-Suche" + ((term.isEmpty()) ? "" : (": " + term));
				SoundtrackDB.setBrowserWindowTitle(title);

				view.showResult(term, searchResult);
			}

			public void onFailure(Throwable caught) {
				handleRpcError("[" + flavor.name() + "] " + AbstractSearchablePresenter.this.lastSearchTerm, caught);
				view.setEnabled(true);
			}
		});
	}

	protected void setLastSearchTerm(String lastSearchTerm) {
		this.lastSearchTerm = lastSearchTerm;
	}

	protected void addHistorySearch(String term) {
		String token = getSearchToken(term);
		getClientFactory().getHistoryManager().createHistory(token, false);
	}
}

package dev.sdb.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.event.FatalErrorEvent;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.QueryView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public abstract class AbstractBrowsePresenter extends AbstractContentPresenter implements QueryView.Presenter {

	private static final Logger LOGGER = Logger.getLogger(AbstractBrowsePresenter.class.getName());

	private final Flavor flavor;

	private String lastSearchTerm;
	private long lastDetailId;

	private Entity currentDetailEntity;

	private DetailView detailView;
	private QueryView queryView;

	public AbstractBrowsePresenter(ClientFactory clientFactory, ContentPresenterType type, Flavor flavor) {
		super(clientFactory, type);
		this.flavor = flavor;
	}

	protected void setLastSearchTerm(String lastSearchTerm) {
		this.lastSearchTerm = lastSearchTerm;
	}

	protected void setLastDetailId(long lastDetailId) {
		this.lastDetailId = lastDetailId;
	}

	public long getLastDetailId() {
		return this.lastDetailId;
	}

	protected abstract DetailView createDetailView();

	protected abstract QueryView createQueryView(String term);

	protected String getEntityToken(ContentPresenterType type, Entity entity) {
		return type.getToken() + "?id=" + entity.getId();
	}

	protected String getSearchToken(String term) {
		return getType().getToken() + "?search=" + term;
	}

	//	

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

	private String getSearchFromState(String state) {
		try {
			return state.substring(7);
		} catch (Exception e) {
			return "";
		}
	}

	private long getIdFromState(String state) {
		try {
			String value = state.substring(3);
			return Long.parseLong(value);
		} catch (Exception e) {
			return 0;
		}
	}

	private IsWidget getDetailView(long id) {
		assert (id > 0);

		if (this.detailView == null) {
			this.detailView = createDetailView();
			setLastDetailId(id);
			getDetailsFromServer(this.detailView);
		} else {
			if (getLastDetailId() != id) {
				setLastDetailId(id);
				getDetailsFromServer(this.detailView);
			}
		}
		return this.detailView;
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

	protected Entity getCurrentDetailEntity() {
		return this.currentDetailEntity;
	}

	protected void setCurrentDetailEntity(Entity currentDetailEntity) {
		this.currentDetailEntity = currentDetailEntity;
	}

	protected void getDetailsFromServer(final DetailView view) {
		setCurrentDetailEntity(null);
		view.initEntity(null);

		//if there's no id, cancel the action
		if (getLastDetailId() <= 0)
			return;
		
		SearchServiceAsync service = getClientFactory().getSearchService();
		
		// Then, we send the input to the server.
		service.get(this.flavor, getLastDetailId(), new AsyncCallback<Entity>() {

			public void onSuccess(Entity entity) {
				String match = entity.getMatch();
				String title = getType().getName() + "-Details" + ((match.isEmpty()) ? "" : (": " + match));
				SoundtrackDB.setBrowserWindowTitle(title);

				setCurrentDetailEntity(entity);
				view.initEntity(entity);
				view.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				handleRpcError("[" + AbstractBrowsePresenter.this.flavor.name() + "] id=" + getLastDetailId(), caught);
				view.setEnabled(true);
			}
		});
	}

	protected void handleRpcError(String message, Throwable caught) {
		String title = "Remote Procedure Call - Error";
		LOGGER.log(Level.SEVERE, title + ":" + message, caught);
		getClientFactory().getEventBus().fireEventFromSource(new FatalErrorEvent(title, message, caught), this);
	}

	@Override public void onBrowse(ContentPresenterType type, Entity entity) {
		addHistoryNavigation(type, entity);
	}

	@Override public void onSearch(String term) {
		setLastSearchTerm(term);
		getSearchFromServer(this.queryView);
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

		// Then, we send the input to the server.
		service.search(this.flavor, term, range, ascending, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				addHistorySearch(term);

				String title = getType().getName() + "-Suche" + ((term.isEmpty()) ? "" : (": " + term));
				SoundtrackDB.setBrowserWindowTitle(title);

				view.showResult(term, searchResult);
			}

			public void onFailure(Throwable caught) {
				handleRpcError("[" + AbstractBrowsePresenter.this.flavor.name() + "] " + AbstractBrowsePresenter.this.lastSearchTerm, caught);
				view.setEnabled(true);
			}
		});
	}

	protected void addHistorySearch(String term) {
		String token = getSearchToken(term);
		getClientFactory().getHistoryManager().createHistory(token, false);
	}

	protected void addHistoryNavigation(ContentPresenterType type, Entity entity) {
		String token = getEntityToken(type, entity);
		getClientFactory().getHistoryManager().createHistory(token, true);
	}
}

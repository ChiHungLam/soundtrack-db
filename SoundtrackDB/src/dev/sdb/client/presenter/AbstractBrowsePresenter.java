package dev.sdb.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.event.FatalErrorEvent;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.DetailView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;

public abstract class AbstractBrowsePresenter extends AbstractContentPresenter implements DetailView.Presenter {

	private static final Logger LOGGER = Logger.getLogger(AbstractBrowsePresenter.class.getName());

	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final Flavor flavor;


	private long lastDetailId;

	private Entity currentDetailEntity;

	private DetailView detailView;


	public AbstractBrowsePresenter(ClientFactory clientFactory, ContentPresenterType type, Flavor flavor) {
		super(clientFactory, type);
		this.flavor = flavor;
	}

	protected Flavor getFlavor() {
		return this.flavor;
	}

	protected void setLastDetailId(long lastDetailId) {
		this.lastDetailId = lastDetailId;
	}

	public long getLastDetailId() {
		return this.lastDetailId;
	}

	protected abstract DetailView createDetailView();



	//	protected String getEntityToken(ContentPresenterType type, Entity entity) {
	//		return type.getToken() + "?id=" + entity.getId();
	//	}



	//	



	protected long getIdFromState(String state) {
		try {
			String value = state.substring(3);
			return Long.parseLong(value);
		} catch (Exception e) {
			return 0;
		}
	}

	protected IsWidget getDetailView(long id) {
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

		message = SERVER_ERROR + "<br><br>" + message;

		getClientFactory().getEventBus().fireEventFromSource(new FatalErrorEvent(title, message, caught), this);
	}

	//	@Override public void onBrowse(ContentPresenterType type, Entity entity) {
	//		addHistoryNavigation(type, entity);
	//	}
	//
	//	protected void addHistoryNavigation(ContentPresenterType type, Entity entity) {
	//		String token = getEntityToken(type, entity);
	//		getClientFactory().getHistoryManager().createHistory(token, true);
	//	}
}

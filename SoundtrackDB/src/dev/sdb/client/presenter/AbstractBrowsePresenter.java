package dev.sdb.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.SearchView;
import dev.sdb.client.view.desktop.detail.DetailWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public abstract class AbstractBrowsePresenter extends AbstractContentPresenter implements SearchView.Presenter {

	private final Flavor flavor;

	private String lastSearchTerm;
	private long lastId;

	private DetailWidget detailWidget;
	private SearchView queryWidget;


	public AbstractBrowsePresenter(ClientFactory clientFactory, ContentPresenterType type, Flavor flavor) {
		super(clientFactory, type);

		this.flavor = flavor;
	}



	protected void setLastSearchTerm(String lastSearchTerm) {
		this.lastSearchTerm = lastSearchTerm;
	}

	protected abstract DetailWidget createDetailWidget();

	protected abstract SearchView createQueryWidget(String term);

	protected String getEntityToken(ContentPresenterType type, Entity entity) {
		return type.getToken() + "?id=" + entity.getId();
	}

	protected String getSearchToken(String term) {
		return getType().getToken() + "?search=" + term;
	}

	//	

	public IsWidget getWidget(String state) {
		assert (state != null);

		if (state.isEmpty())
			return getQueryWidget("");

		if (state.startsWith("search=")) {
			return getQueryWidget(getSearchFromState(state));

		} else if (state.startsWith("id=")) {
			long id = getIdFromState(state);
			if (id > 0) {
				return getDetailWidget(id);
			}
		}
		return getQueryWidget("");
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

	private Widget getDetailWidget(long id) {
		assert (id > 0);

		if (this.detailWidget == null) {
			this.detailWidget = createDetailWidget();
			this.lastId = id;
			getDetailsFromServer(this.detailWidget);
		} else {
			Entity entity = this.detailWidget.getCurrentEntity();
			long currentId = (entity == null) ? 0 : entity.getId();
			if (currentId != id) {
				this.lastId = id;
				getDetailsFromServer(this.detailWidget);
			}
		}
		return this.detailWidget;
	}


	private SearchView getQueryWidget(String term) {
		assert (term != null);

		if (this.queryWidget == null) {
			this.queryWidget = createQueryWidget(term);
			if (!term.isEmpty()) {
				this.lastSearchTerm = term;
				getSearchFromServer(this.queryWidget);
			}
		} else {
			if (!this.queryWidget.getText().equalsIgnoreCase(term)) {
				this.queryWidget.setText(term);
				this.lastSearchTerm = term;

				if (term.isEmpty()) {
					this.queryWidget.reset();
				} else {
					getSearchFromServer(this.queryWidget);
				}
			}
		}
		return this.queryWidget;
	}

	protected void getDetailsFromServer(final DetailWidget detailWidget) {
		detailWidget.initEntity(null);

		//if there's no id, cancel the action
		if (this.lastId <= 0)
			return;
		
		SearchServiceAsync service = getClientFactory().getSearchService();
		
		// Then, we send the input to the server.
		service.get(this.flavor, this.lastId, new AsyncCallback<Entity>() {

			public void onSuccess(Entity entity) {
				detailWidget.initEntity(entity);
				detailWidget.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showRpcError(caught, "[" + AbstractBrowsePresenter.this.flavor.name() + "] id=" + AbstractBrowsePresenter.this.lastId, detailWidget);
			}
		});
	}

	@Override public void onBrowse(Entity entity) {
		addHistoryNavigation(getType(), entity);
	}

	@Override public void onSearch(String term) {
		setLastSearchTerm(term);
		getSearchFromServer(this.queryWidget);
	}

	protected void getSearchFromServer(final SearchView queryWidget) {
		//		final SearchField search = queryWidget.getSearchField();
		//		final ResultField result = queryWidget.getResultField();

		//if there hasn't been a search before, cancel the action



		if (this.lastSearchTerm == null) {
			queryWidget.reset();
			return;
		}

		queryWidget.lockForSearch();

		//		final DataGrid<Entity> table = result.getDataGrid();
		final Range range = queryWidget.getRange();//table.getVisibleRange();
		final boolean ascending = queryWidget.isSortAscending();//true; //sortInfo.isAscending();

		final String term = this.lastSearchTerm;

		SearchServiceAsync service = getClientFactory().getSearchService();

		// Then, we send the input to the server.
		service.search(this.flavor, term, range, ascending, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				addHistorySearch(term);
				queryWidget.showResult(term, searchResult);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showRpcError(caught, "[" + AbstractBrowsePresenter.this.flavor.name() + "] " + AbstractBrowsePresenter.this.lastSearchTerm, queryWidget);

				//				result.setElementVisibility(-1);
			}
		});
	}

	protected void addHistorySearch(String term) {
		String token = getSearchToken(term);
		getClientFactory().getHistoryManager().setHistory(token, false);
	}

	protected void addHistoryNavigation(ContentPresenterType type, Entity entity) {
		String token = getEntityToken(type, entity);
		getClientFactory().getHistoryManager().setHistory(token, true);
	}

	protected void showRpcError(Throwable caught, String msg, final HasEnabled hasEnabled) {
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call - Fehler");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");

		final Label textToServerLabel = new Label();
		textToServerLabel.setText(msg);

		final HTML serverResponseLabel = new HTML();
		serverResponseLabel.setText("");
		serverResponseLabel.addStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(SERVER_ERROR + "<p>Original error message:<br/>" + caught.getLocalizedMessage() + "</p>");

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sent to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				if (hasEnabled != null)
					hasEnabled.setEnabled(true);
			}
		});

		// Show the RPC error message to the user
		dialogBox.center();
		closeButton.setFocus(true);
	}


}

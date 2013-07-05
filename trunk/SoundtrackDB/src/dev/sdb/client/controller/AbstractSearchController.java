package dev.sdb.client.controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.service.SearchService;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.search.QueryWidget;
import dev.sdb.client.ui.search.ResultField;
import dev.sdb.client.ui.search.SearchEvent;
import dev.sdb.client.ui.search.SearchEventHandler;
import dev.sdb.client.ui.search.SearchField;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.SearchResult;
import dev.sdb.shared.model.entity.Entity;

public abstract class AbstractSearchController implements Controller {

	/**
	 * Create a remote service proxy to talk to the server-side Search service.
	 */
	private static final SearchServiceAsync SEARCH_SERVICE = GWT.create(SearchService.class);

	private final SoundtrackDB sdb;
	private final ControllerType type;
	private final Flavor flavor;

	private String lastSearchTerm;
	private long lastId;
	private DetailWidget detailWidget;
	private QueryWidget queryWidget;
	private Column<Entity, ?> rendererColumn;

	public AbstractSearchController(SoundtrackDB sdb, ControllerType type, Flavor flavor) {
		super();
		this.sdb = sdb;
		this.type = type;
		this.flavor = flavor;
	}

	@Override public ControllerType getType() {
		return this.type;
	}

	protected void setLastSearchTerm(String lastSearchTerm) {
		this.lastSearchTerm = lastSearchTerm;
	}

	protected abstract DetailWidget createDetailWidget();

	private QueryWidget createQueryWidget(String term) {
		// Create the query widget instance
		final QueryWidget queryWidget = new QueryWidget();

		// Get the search and result fields
		final SearchField search = queryWidget.getSearchField();
		final ResultField result = queryWidget.getResultField();

		// Set the search term
		search.setText(term);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSearchFromServer(queryWidget);
			}
		};

		// Create the renderer column.
		Column<Entity, ?> column = getRendererColumn();

		// Init result field with the column and the data provider
		result.init(column, dataProvider, VISIBLE_RANGE_LENGTH);

		// Get the result table
		final CellTable<Entity> table = result.getTable();
		//		table.getSelectionModel().

		//Add a search event handler to send the search to the server, when the user chooses to
		search.addSearchEventHandler(new SearchEventHandler() {
			/**
			 * Fired when the user clicks on the search button or hits enter in the search text field. Sends the name
			 * from the nameField to the server and waits for a response.
			 */
			@Override public void onSearch(SearchEvent event) {
				String term = event.getSearchTerm();
				setLastSearchTerm(term);

				table.setVisibleRange(0, VISIBLE_RANGE_LENGTH);
				getSearchFromServer(queryWidget);
			}
		});

		final SingleSelectionModel<Entity> selectionModel = new SingleSelectionModel<Entity>();
		table.setSelectionModel(selectionModel);

		table.addDomHandler(new DoubleClickHandler() {
			@Override public void onDoubleClick(final DoubleClickEvent event) {
				Entity entity = selectionModel.getSelectedObject();
				if (entity != null) {
					History.newItem(AbstractSearchController.this.type.getToken() + "?id=" + entity.getId());
				}
			}
		}, DoubleClickEvent.getType());

		return queryWidget;
	}

	protected abstract Column<Entity, ?> createRendererColumn();

	public Column<Entity, ?> getRendererColumn() {
		if (this.rendererColumn == null)
			this.rendererColumn = createRendererColumn();
		return this.rendererColumn;
	}


	public Widget getWidget(String state) {
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
			if (this.detailWidget.getCurrentId() != id) {
				this.lastId = id;
				getDetailsFromServer(this.detailWidget);
			}
		}
		return this.detailWidget;
	}


	private QueryWidget getQueryWidget(String term) {
		assert (term != null);

		if (this.queryWidget == null) {
			this.queryWidget = createQueryWidget(term);
			if (!term.isEmpty()) {
				this.lastSearchTerm = term;
				getSearchFromServer(this.queryWidget);
			}
		} else {
			if (!this.queryWidget.getSearchField().getText().equalsIgnoreCase(term)) {
				this.queryWidget.getSearchField().setText(term);
				this.lastSearchTerm = term;

				if (term.isEmpty()) {
					this.queryWidget.getResultField().setElementVisibility(-1);
				} else {
					getSearchFromServer(this.queryWidget);
				}
			}
		}
		return this.queryWidget;
	}

	protected void getDetailsFromServer(final DetailWidget detailWidget) {
		//if there's no id, cancel the action
		if (this.lastId <= 0)
			return;
		
		
		// Then, we send the input to the server.
		SEARCH_SERVICE.get(this.flavor, this.lastId, new AsyncCallback<Entity>() {

			public void onSuccess(Entity entity) {
				//String token = getType().getToken() + "?search=" + AbstractSearchController.this.lastSearchTerm;
				//History.newItem(token, false);
				//AbstractSearchController.this.sdb.setToken(token);

				detailWidget.initEntity(entity);
				detailWidget.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();

				detailWidget.initEntity(null);

				// Create the popup dialog box
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Remote Procedure Call - Fehler");
				dialogBox.setAnimationEnabled(true);
				final Button closeButton = new Button("Close");

				// We can set the id of a widget by accessing its Element
				closeButton.getElement().setId("closeButton");

				final Label textToServerLabel = new Label();
				textToServerLabel.setText("id=" + AbstractSearchController.this.lastId);

				final HTML serverResponseLabel = new HTML();
				serverResponseLabel.setText("");
				serverResponseLabel.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);

				VerticalPanel dialogVPanel = new VerticalPanel();
				dialogVPanel.addStyleName("dialogVPanel");
				dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
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
						detailWidget.setEnabled(true);
					}
				});

				// Show the RPC error message to the user

				dialogBox.center();
				closeButton.setFocus(true);
			}
		});

	}

	protected void getSearchFromServer(QueryWidget queryWidget) {
		final SearchField search = queryWidget.getSearchField();
		final ResultField result = queryWidget.getResultField();

		//if there hasn't been a search before, cancel the action
		if (this.lastSearchTerm == null)
			return;

		final CellTable<Entity> table = result.getTable();
		final Range range = table.getVisibleRange();
		final ColumnSortInfo sortInfo = table.getColumnSortList().get(0);
		final boolean ascending = sortInfo.isAscending();

		//Disable search
		search.setEnabled(false);

		// Then, we send the input to the server.
		SEARCH_SERVICE.search(this.flavor, this.lastSearchTerm, range, ascending, new AsyncCallback<SearchResult>() {

			public void onSuccess(SearchResult searchResult) {
				String token = getType().getToken() + "?search=" + AbstractSearchController.this.lastSearchTerm;
				History.newItem(token, false);
				AbstractSearchController.this.sdb.setToken(token);

				int total = searchResult.getTotalLength();

				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());
				result.setText(searchResult.getInfo());
				result.setElementVisibility(total);
				search.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();

				result.setElementVisibility(-1);

				// Create the popup dialog box
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Remote Procedure Call - Fehler");
				dialogBox.setAnimationEnabled(true);
				final Button closeButton = new Button("Close");

				// We can set the id of a widget by accessing its Element
				closeButton.getElement().setId("closeButton");

				final Label textToServerLabel = new Label();
				textToServerLabel.setText(AbstractSearchController.this.lastSearchTerm);

				final HTML serverResponseLabel = new HTML();
				serverResponseLabel.setText("");
				serverResponseLabel.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);

				VerticalPanel dialogVPanel = new VerticalPanel();
				dialogVPanel.addStyleName("dialogVPanel");
				dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
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
						search.setEnabled(true);
					}
				});

				// Show the RPC error message to the user

				dialogBox.center();
				closeButton.setFocus(true);
			}
		});

	}
}

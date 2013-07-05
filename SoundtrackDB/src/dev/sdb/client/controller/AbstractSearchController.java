package dev.sdb.client.controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.service.SearchService;
import dev.sdb.client.service.SearchServiceAsync;
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

	private SoundtrackDB sdb;
	private String lastSearchTerm;
	private Flavor flavor;
	private ControllerType type;

	private QueryWidget widget;

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

	private QueryWidget createQueryWidget(String term) {
		// Create the query widget instance
		QueryWidget queryWidget = new QueryWidget();

		// Get the search and result fields
		final SearchField search = queryWidget.getSearchField();
		final ResultField result = queryWidget.getResultField();

		// Set the search term
		search.setText(term);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				doSearchOnServer(search, result);
			}
		};

		// Create the renderer column.
		Column<Entity, ?> column = getRendererColumn();

		// Init result field with the column and the data provider
		result.init(column, dataProvider, VISIBLE_RANGE_LENGTH);

		//Add a search event handler to send the search to the server, when the user chooses to
		search.addSearchEventHandler(new SearchEventHandler() {
			/**
			 * Fired when the user clicks on the search button or hits enter in the search text field. Sends the name
			 * from the nameField to the server and waits for a response.
			 */
			@Override public void onSearch(SearchEvent event) {
				String term = event.getSearchTerm();
				setLastSearchTerm(term);
				final CellTable<Entity> table = result.getTable();
				table.setVisibleRange(0, VISIBLE_RANGE_LENGTH);
				doSearchOnServer(search, result);
			}
		});

		return queryWidget;
	}

	protected abstract Column<Entity, ?> createRendererColumn();

	public Column<Entity, ?> getRendererColumn() {
		if (this.rendererColumn == null)
			this.rendererColumn = createRendererColumn();
		return this.rendererColumn;
	}


	public Widget getWidget(String state) {
		if (state == null || state.isEmpty())
			return getQueryWidget(null);

		if (state.startsWith("search=")) {
			return getQueryWidget(state.substring(7));
		} else {
			return getQueryWidget("");
		}
	}

	private QueryWidget getQueryWidget(String term) {
		if (this.widget == null) {
			this.widget = createQueryWidget(term);
			if (term != null && !term.isEmpty()) {
				this.lastSearchTerm = term;
				doSearchOnServer(this.widget.getSearchField(), this.widget.getResultField());
			}
		} else {
			if (!this.widget.getSearchField().getText().equalsIgnoreCase(term)) {
				if (term == null)
					term = "";

				this.widget.getSearchField().setText(term);
				this.lastSearchTerm = term;

				if (term.isEmpty()) {
					this.widget.getResultField().setElementVisibility(-1);
				} else {
					doSearchOnServer(this.widget.getSearchField(), this.widget.getResultField());
				}
			}
		}
		return this.widget;
	}


	protected void doSearchOnServer(final SearchField search, final ResultField result) {

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
		SEARCH_SERVICE.search(this.lastSearchTerm, this.flavor, range, ascending, new AsyncCallback<SearchResult>() {

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
				dialogBox.setText("RPC - Fehler");
				//				dialogBox.setText("Remote Procedure Call");
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

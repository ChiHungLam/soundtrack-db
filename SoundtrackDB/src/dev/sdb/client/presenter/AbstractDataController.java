package dev.sdb.client.presenter;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
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
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public abstract class AbstractDataController implements Controller {

	/**
	 * Create a remote service proxy to talk to the server-side Search service.
	 */
	protected static final SearchServiceAsync SEARCH_SERVICE = GWT.create(SearchService.class);

	private final SoundtrackDB sdb;
	private final ControllerType type;
	private final Flavor flavor;

	private String lastSearchTerm;
	private long lastId;

	private DetailWidget detailWidget;
	private QueryWidget queryWidget;


	public AbstractDataController(SoundtrackDB sdb, ControllerType type, Flavor flavor) {
		super();
		this.sdb = sdb;
		this.type = type;
		this.flavor = flavor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dev.sdb.client.controller.Controller#getType()
	 */
	@Override public ControllerType getType() {
		return this.type;
	}

	protected boolean isSearchResultCompactView() {
		return true;
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

		// Get the result table
		final DataGrid<Entity> table = result.getDataGrid();

		// Add the columns.
		addSearchResultColumns(table);

		table.setWidth("100%");

		// Set the total row count. You might send an RPC request to determine the
		//		 total row count.
		table.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		table.setVisibleRange(0, VISIBLE_RANGE_LENGTH);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSearchFromServer(queryWidget);
			}
		};
		dataProvider.addDataDisplay(table);

		// Add a ColumnSortEvent.AsyncHandler to connect sorting to the
		// AsyncDataPRrovider.
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);

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
					addHistoryNavigation(getType(), entity);
				}
			}
		}, DoubleClickEvent.getType());

		return queryWidget;
	}

	protected String getEntityToken(ControllerType type, Entity entity) {
		return type.getToken() + "?id=" + entity.getId();
	}

	protected String getSearchToken(String term) {
		return getType().getToken() + "?search=" + term;
	}

	protected abstract void addSearchResultColumns(DataGrid<Entity> table);

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
			Entity entity = this.detailWidget.getCurrentEntity();
			long currentId = (entity == null) ? 0 : entity.getId();
			if (currentId != id) {
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
		detailWidget.initEntity(null);

		//if there's no id, cancel the action
		if (this.lastId <= 0)
			return;
		
		
		// Then, we send the input to the server.
		SEARCH_SERVICE.get(this.flavor, this.lastId, new AsyncCallback<Entity>() {

			public void onSuccess(Entity entity) {
				detailWidget.initEntity(entity);
				detailWidget.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showRpcError(caught, "[" + AbstractDataController.this.flavor.name() + "] id=" + AbstractDataController.this.lastId, detailWidget);
			}
		});
	}

	protected void getSearchFromServer(QueryWidget queryWidget) {
		final SearchField search = queryWidget.getSearchField();
		final ResultField result = queryWidget.getResultField();

		//if there hasn't been a search before, cancel the action
		if (this.lastSearchTerm == null) {
			result.setElementVisibility(-1);
			return;
		}

		final DataGrid<Entity> table = result.getDataGrid();
		final Range range = table.getVisibleRange();
		//		final ColumnSortInfo sortInfo = table.getColumnSortList().get(0);
		final boolean ascending = true; //sortInfo.isAscending();

		final String term = this.lastSearchTerm;

		//Disable search
		search.setEnabled(false);

		// Then, we send the input to the server.
		SEARCH_SERVICE.search(this.flavor, term, range, ascending, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				addHistorySearch(term);

				int total = searchResult.getTotalLength();

				String lastSearch = "Suchergebnis für: '" + term + "'.";

				String resultInfo = "";
				if (total == 0) {
					resultInfo = "Es wurden keine Einträge gefunden.";
				} else if (total > 0) {
					resultInfo = "Es " + (total == 1 ? "wurde 1 Eintrag" : ("wurden " + total + " Einträge")) + " gefunden.";
				}

				//				table.setPageSize(10);
				//				table.setPageStart(0);
				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());

				result.setLastSearchText(lastSearch);
				result.setSelectionInfoText(resultInfo);
				result.setElementVisibility(total);
				search.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showRpcError(caught, "[" + AbstractDataController.this.flavor.name() + "] " + AbstractDataController.this.lastSearchTerm, search);

				result.setElementVisibility(-1);
			}
		});

	}

	protected void addHistorySearch(String term) {
		String token = getSearchToken(term);
		this.sdb.setHistory(token, false);
	}

	protected void addHistoryNavigation(ControllerType type, Entity entity) {
		String token = getEntityToken(type, entity);
		this.sdb.setHistory(token, true);
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

	private Column<Entity, SafeHtml> createCompactReleaseColumn() {
		final SafeHtmlCell releaseCell = new SafeHtmlCell();
		Column<Entity, SafeHtml> releaseColumn = new Column<Entity, SafeHtml>(releaseCell) {
			@Override public SafeHtml getValue(Entity entity) {
				if (entity == null)
					return null;

				Release release;
				if (entity instanceof Soundtrack)
					release = ((Soundtrack) entity).getRelease();
				else
					release = (Release) entity;

				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<b>" + release.getTitleInfo() + "</b>");
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant(release.getCatalogInfo());
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant("<i>" + release.getType() + " von " + release.getYearInfo() + "</i>");
				return sb.toSafeHtml();
			}
		};
		return releaseColumn;
	}

	private Column<Entity, SafeHtml> createCompactMusicColumn() {
		final SafeHtmlCell musicCell = new SafeHtmlCell();
		Column<Entity, SafeHtml> musicColumn = new Column<Entity, SafeHtml>(musicCell) {
			@Override public SafeHtml getValue(Entity entity) {
				if (entity == null)
					return null;

				Music music;
				if (entity instanceof Soundtrack)
					music = ((Soundtrack) entity).getMusic();
				else
					music = (Music) entity;

				String authors = music.getAuthors();
				String artist = music.getArtist();
				if (artist != null)
					artist += " (" + music.getYearInfo() + ")";

				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant((artist == null) ? "&nbsp;" : artist);
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant("<b>" + music.getTitleInfo() + "</b>");
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant((authors == null) ? "&nbsp;" : ("<i>(" + authors + ")</i>"));
				return sb.toSafeHtml();
			}
		};
		return musicColumn;
	}

	public void addSoundtrackColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, 30.0, Unit.PX);

		// Add the release column(s)
		addReleaseColumns(table, compact, false);

		// Add the music column(s)
		addMusicColumns(table, compact, false);

		if (push)
			table.getColumnSortList().push(soundtrackColumn);
	}

	public void addReleaseColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		if (compact) {
			Column<Entity, SafeHtml> releaseColumn = createCompactReleaseColumn();
			// Make the columns sortable.
			releaseColumn.setSortable(true);

			table.addColumn(releaseColumn, "Veröffentlichung");
			table.setColumnWidth(releaseColumn, 300.0, Unit.PX);

			if (push)
				table.getColumnSortList().push(releaseColumn);
			return;
		}

		TextColumn<Entity> typeColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getType();
			}
		};

		TextColumn<Entity> catColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getCatalogInfo();
			}
		};

		TextColumn<Entity> yearColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getYearInfo();
			}
		};

		TextColumn<Entity> titleColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getTitleInfo();
			}
		};

		//		 Make the columns sortable.
		titleColumn.setSortable(true);

		// Add the columns.
		table.addColumn(typeColumn, "Typ");
		table.setColumnWidth(typeColumn, 50.0, Unit.PX);

		table.addColumn(catColumn, "Kat.-Nr.");
		table.setColumnWidth(catColumn, 150.0, Unit.PX);

		table.addColumn(yearColumn, "Jahr");
		table.setColumnWidth(yearColumn, 50.0, Unit.PX);

		table.addColumn(titleColumn, "Titel");
		table.setColumnWidth(titleColumn, 150.0, Unit.PX);

		if (push)
			table.getColumnSortList().push(titleColumn);

	}

	public void addMusicColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		if (compact) {
			Column<Entity, SafeHtml> musicColumn = createCompactMusicColumn();
			// Make the columns sortable.
			musicColumn.setSortable(true);

			table.addColumn(musicColumn, "Musik");
			table.setColumnWidth(musicColumn, 300.0, Unit.PX);

			if (push)
				table.getColumnSortList().push(musicColumn);
			return;
		}

		TextColumn<Entity> genreColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getGenre().getChildName();
			}
		};

		TextColumn<Entity> yearColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "" + ((Music) entity).getYear();
			}
		};

		TextColumn<Entity> artistColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getArtist();
			}
		};

		TextColumn<Entity> titleColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getTitleInfo();
			}
		};

		// Make the columns sortable.
		titleColumn.setSortable(true);

		// Add the columns.
		table.addColumn(genreColumn, "Genre");
		table.setColumnWidth(genreColumn, 50.0, Unit.PX);

		table.addColumn(yearColumn, "Jahr");
		table.setColumnWidth(yearColumn, 50.0, Unit.PX);

		table.addColumn(artistColumn, "Interpret");
		table.setColumnWidth(artistColumn, 150.0, Unit.PX);

		table.addColumn(titleColumn, "Titel");
		table.setColumnWidth(titleColumn, 200.0, Unit.PX);

		if (push)
			table.getColumnSortList().push(titleColumn);
	}

	public void addReleaseMusicColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, 30.0, Unit.PX);

		// Add the music column(s)
		addMusicColumns(table, compact, false);

		if (push)
			table.getColumnSortList().push(soundtrackColumn);
	}
}

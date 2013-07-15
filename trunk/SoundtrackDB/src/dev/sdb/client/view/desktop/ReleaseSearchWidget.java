package dev.sdb.client.view.desktop;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;

import dev.sdb.client.view.ReleaseSearchView;
import dev.sdb.client.view.desktop.search.ResultField;
import dev.sdb.client.view.desktop.search.SearchEvent;
import dev.sdb.client.view.desktop.search.SearchField;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public class ReleaseSearchWidget extends Composite implements ReleaseSearchView {

	interface ReleaseSearchWidgetUiBinder extends UiBinder<Widget, ReleaseSearchWidget> {}
	private static ReleaseSearchWidgetUiBinder uiBinder = GWT.create(ReleaseSearchWidgetUiBinder.class);

	private static final int VISIBLE_RANGE_LENGTH = 10;
	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private Presenter presenter;

	@UiField SearchField searchField;
	@UiField ResultField resultField;

	public ReleaseSearchWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		DataGrid<Entity> table = this.resultField.getDataGrid();

		// Add the columns.
		addReleaseColumns(table, true, true);

		table.setWidth("100%");

		// Set the total row count. You might send an RPC request to determine the
		//		 total row count.
		table.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		table.setVisibleRange(0, VISIBLE_RANGE_LENGTH);

		// Add a ColumnSortEvent.AsyncHandler to connect sorting to the
		// AsyncDataPRrovider.
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);

		this.resultField.setElementVisibility(-1);
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

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("searchField")
	void onSearchFieldSearch(SearchEvent event) {
		this.presenter.searchRelease(event.getSearchTerm());
	}

	@Override public void lockForSearch() {
		this.searchField.setEnabled(false);
		this.resultField.getDataGrid().setVisibleRange(0, VISIBLE_RANGE_LENGTH);
	}

	@Override public Range getRange() {
		return this.resultField.getDataGrid().getVisibleRange();
	}

	@Override public boolean isSortAscending() {
		//		final ColumnSortInfo sortInfo = table.getColumnSortList().get(0);
		return true; //sortInfo.isAscending();
	}

	@Override public void showSearchError(Throwable caught) {
		showRpcError(caught, "[" + Flavor.RELEASES.name() + "] " + this.searchField.getText(), this.searchField);

		this.resultField.setElementVisibility(-1);
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

	@Override public void showSearchResult(Result searchResult) {
		int total = searchResult.getTotalLength();

		String lastSearch = "Suchergebnis für: '" + searchResult.getTerm() + "'.";

		String resultInfo = "";
		if (total == 0) {
			resultInfo = "Es wurden keine Einträge gefunden.";
		} else if (total > 0) {
			resultInfo = "Es " + (total == 1 ? "wurde 1 Eintrag" : ("wurden " + total + " Einträge")) + " gefunden.";
		}

		DataGrid<Entity> table = this.resultField.getDataGrid();

		//				table.setPageSize(10);
		//				table.setPageStart(0);
		table.setRowCount(total, true);
		table.setRowData(searchResult.getResultStart(), searchResult.getResultChunk());

		this.resultField.setLastSearchText(lastSearch);
		this.resultField.setSelectionInfoText(resultInfo);
		this.resultField.setElementVisibility(total);
		this.searchField.setEnabled(true);
	}

	@Override public void setDataProvider(AsyncDataProvider<Entity> dataProvider) {
		dataProvider.addDataDisplay(this.resultField.getDataGrid());

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

	@Override public void setRange(int rangeStart, int rangeLength) {
		this.resultField.getDataGrid().setVisibleRange(rangeStart, rangeLength);
	}

	@Override public void setSortAscending(boolean sortAscending) {
		// TODO Auto-generated method stub
		//		this.resultField.getDataGrid().getColumnSortList().get(0).
	}
}

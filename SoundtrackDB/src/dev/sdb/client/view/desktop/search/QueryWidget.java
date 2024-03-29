package dev.sdb.client.view.desktop.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

import dev.sdb.client.event.SearchEvent;
import dev.sdb.client.event.SearchEvent.Mode;
import dev.sdb.client.event.SearchEventHandler;
import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.QueryView;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public abstract class QueryWidget extends Composite implements QueryView {

	private static final int VISIBLE_RANGE_LENGTH = 20;

	interface QueryWidgetUiBinder extends UiBinder<Widget, QueryWidget> {}
	private static QueryWidgetUiBinder uiBinder = GWT.create(QueryWidgetUiBinder.class);

	public interface Style extends CssResource {
		String error();
	}

	@UiField protected Style style;

	@UiField SearchField searchField;
	@UiField ResultField resultField;

	private QueryView.Presenter presenter;

	public QueryWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		// Get the result table
		final CellTable<Entity> table = getResultTable();

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

		final SingleSelectionModel<Entity> selectionModel = new SingleSelectionModel<Entity>();
		table.setSelectionModel(selectionModel);

		//Add a search event handler to send the search to the server, when the user chooses to
		this.searchField.addSearchEventHandler(new SearchEventHandler() {
			/**
			 * Fired when the user clicks on the search button or hits enter in the search text field. Sends the name
			 * from the nameField to the server and waits for a response.
			 */
			@Override public void onSearch(SearchEvent event) {
				SearchEvent.Mode mode = event.getMode();
				String value = event.getValue();

				if (mode == Mode.VALID) {
					// Clear any previous error message
					clearErrorDisplay();
					QueryWidget.this.presenter.onSearch(value);
				} else {
					// Show error message
					showErrorDisplay(value);
				}
			}
		});
	}

	protected void showErrorDisplay(String message) {
		this.searchField.showErrorDisplay(message);
		this.searchField.addStyleName(this.style.error());
		this.resultField.addStyleName(this.style.error());
	}

	protected void clearErrorDisplay() {
		this.searchField.clearErrorDisplay();
		this.searchField.removeStyleName(this.style.error());
		this.resultField.removeStyleName(this.style.error());
	}

	protected abstract ContentPresenterType getContentPresenterType();


	@Override public CellTable<Entity> getResultTable() {
		return this.resultField.getTable();
	}

	@Override public void setEnabled(boolean enabled) {
		this.searchField.setEnabled(enabled);
	}

	@Override public boolean isEnabled() {
		return this.searchField.isEnabled();
	}



	@Override public Range getRange() {
		return this.resultField.getTable().getVisibleRange();
	}

	@Override public boolean isSortAscending() {
		//		final ColumnSortInfo sortInfo = table.getColumnSortList().get(0);
		return true;
	}

	@Override public void lockForSearch() {
		//		this.resultField.setElementVisibility(-1);
		//Disable search
		this.searchField.setEnabled(false);
	}

	@Override public void reset() {
		this.resultField.setElementVisibility(-1);
		this.resultField.getTable().setVisibleRange(0, VISIBLE_RANGE_LENGTH);
		//Disable search
		this.searchField.setEnabled(true);
	}

	@Override public void setPresenter(QueryView.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override public void setDataProvider(AsyncDataProvider<Entity> dataProvider) {
		dataProvider.addDataDisplay(this.resultField.getTable());
	}

	public SearchField getSearchField() {
		return this.searchField;
	}

	public ResultField getResultField() {
		return this.resultField;
	}

	@Override public String getText() {
		return getSearchField().getText();
	}

	@Override public void setText(String text) {
		getSearchField().setText(text);
	}

	@Override public void showResult(String term, Result searchResult) {
		int total = searchResult.getTotalLength();

		String lastSearch = "Suchergebnis für: '" + term + "'.";

		String resultInfo = "";
		if (total == 0) {
			resultInfo = "Es wurden keine Einträge gefunden.";
		} else if (total > 0) {
			resultInfo = "Es " + (total == 1 ? "wurde 1 Eintrag" : ("wurden " + total + " Einträge")) + " gefunden.";
		}

		CellTable<Entity> table = this.resultField.getTable();

		//				table.setPageSize(10);
		//				table.setPageStart(0);
		table.setRowCount(total, true);
		table.setRowData(searchResult.getRangeStart(), searchResult.getResultChunk());

		this.resultField.setLastSearchText(lastSearch);
		this.resultField.setSelectionInfoText(resultInfo);
		this.resultField.setElementVisibility(total);
		this.searchField.setEnabled(true);
	}

}

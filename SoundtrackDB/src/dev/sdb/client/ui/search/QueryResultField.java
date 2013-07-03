package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;

import dev.sdb.shared.model.SearchResultSortType;
import dev.sdb.shared.model.SoundtrackContainer;

public class QueryResultField extends AbstractResultField {

	interface ResultFieldUiBinder extends UiBinder<Widget, QueryResultField> {}
	private static ResultFieldUiBinder uiBinder = GWT.create(ResultFieldUiBinder.class);

	@UiField CellTable<SoundtrackContainer> cellTable;
	@UiField SimplePager pager;
	@UiField Label infoLabel;
	@UiField VerticalPanel tablePanel;
	@UiField Label emptyResultLabel;

	private SearchResultSortType lastSortType;

	public QueryResultField() {
		super();
		this.cellTable = new CellTable<SoundtrackContainer>();
		initWidget(uiBinder.createAndBindUi(this));

		this.pager.setDisplay(this.cellTable);
		setElementVisibility(-1);
	}

	public CellTable<SoundtrackContainer> getTable() {
		return this.cellTable;
	}

	public SearchResultSortType getSortType() {
		return this.lastSortType;
	}

	public void init(AsyncDataProvider<SoundtrackContainer> dataProvider, int rangeLength) {
		// Create type column.
		final TextColumn<SoundtrackContainer> typeColumn = new TextColumn<SoundtrackContainer>() {
			@Override public String getValue(SoundtrackContainer container) {
				return container.getType().toString();
			}
		};

		// Create title column.
		final TextColumn<SoundtrackContainer> titleColumn = new TextColumn<SoundtrackContainer>() {
			@Override public String getValue(SoundtrackContainer container) {
				return container.getTitle();
			}
		};

		// Make the columns sortable.
		typeColumn.setSortable(true);
		titleColumn.setSortable(true);

		// Add the columns.
		this.cellTable.addColumn(typeColumn, "Typ");
		this.cellTable.addColumn(titleColumn, "Titel");

		this.cellTable.setWidth("100%", true);
		this.cellTable.setColumnWidth(typeColumn, 10.0, Unit.PCT);
		this.cellTable.setColumnWidth(titleColumn, 90.0, Unit.PCT);

		// Set the total row count. You might send an RPC request to determine the
		// total row count.
		this.cellTable.setRowCount(0, true);//CONTACTS.size()

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		this.cellTable.setVisibleRange(0, rangeLength);

		dataProvider.addDataDisplay(this.cellTable);

		// Add a ColumnSortEvent.AsyncHandler to connect sorting to the
		// AsyncDataPRrovider.
		AsyncHandler columnSortHandler = new AsyncHandler(this.cellTable) {
			@Override public void onColumnSort(ColumnSortEvent event) {
				if (event.getColumn() == typeColumn)
					QueryResultField.this.lastSortType = SearchResultSortType.TYPE;
				else if (event.getColumn() == titleColumn)
					QueryResultField.this.lastSortType = SearchResultSortType.TITLE;
				else
					QueryResultField.this.lastSortType = null;
				super.onColumnSort(event);
			}
		};
		this.cellTable.addColumnSortHandler(columnSortHandler);

		// We know that the data is sorted alphabetically by default.
		this.cellTable.getColumnSortList().push(typeColumn);
		this.lastSortType = SearchResultSortType.TYPE;
	}

	@Override public String getText() {
		return this.infoLabel.getText();
	}

	@Override public void setText(String text) {
		this.infoLabel.setText(text);
	}

	public void setElementVisibility(int numRows) {
		this.tablePanel.setVisible(numRows > 0);
		this.emptyResultLabel.setVisible(numRows == 0);
	}

}

package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;

import dev.sdb.shared.model.entity.Entity;

public class ResultField extends Composite implements HasText {

	interface ResultFieldUiBinder extends UiBinder<Widget, ResultField> {}
	private static ResultFieldUiBinder uiBinder = GWT.create(ResultFieldUiBinder.class);

	@UiField(provided = true) CellTable<Entity> cellTable;
	@UiField(provided = true) SimplePager pager;

	@UiField Label infoLabel;
	@UiField VerticalPanel tablePanel;
	@UiField Label emptyResultLabel;

	public ResultField() {
		super();
		this.cellTable = new CellTable<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		initWidget(uiBinder.createAndBindUi(this));

		this.pager.setDisplay(this.cellTable);
		setElementVisibility(-1);
	}

	public CellTable<Entity> getTable() {
		return this.cellTable;
	}

	public void init(Column<Entity, ?> column, AsyncDataProvider<Entity> dataProvider, int rangeLength) {

		// Make the columns sortable.
		column.setSortable(true);

		// Add the columns.
		this.cellTable.addColumn(column, "Titel");

		this.cellTable.setWidth("100%", true);
		this.cellTable.setColumnWidth(column, 100.0, Unit.PCT);

		// Set the total row count. You might send an RPC request to determine the
		// total row count.
		this.cellTable.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		this.cellTable.setVisibleRange(0, rangeLength);

		dataProvider.addDataDisplay(this.cellTable);

		// Add a ColumnSortEvent.AsyncHandler to connect sorting to the
		// AsyncDataPRrovider.
		AsyncHandler columnSortHandler = new AsyncHandler(this.cellTable);
		this.cellTable.addColumnSortHandler(columnSortHandler);

		// We know that the data is sorted alphabetically by default.
		this.cellTable.getColumnSortList().push(column);
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
		if (numRows < 0)
			setText("");
	}
}

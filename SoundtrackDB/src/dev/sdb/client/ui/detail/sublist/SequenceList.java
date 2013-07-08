package dev.sdb.client.ui.detail.sublist;

import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;

public class SequenceList extends Composite {

	interface SequenceListUiBinder extends UiBinder<Widget, SequenceList> {}
	private static SequenceListUiBinder uiBinder = GWT.create(SequenceListUiBinder.class);

	@UiField(provided = true) CellTable<Entity> cellTable;
	@UiField(provided = true) SimplePager pager;

	public SequenceList() {
		super();
		this.cellTable = new CellTable<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		initWidget(uiBinder.createAndBindUi(this));

		this.pager.setDisplay(this.cellTable);
	}

	public CellTable<Entity> getTable() {
		return this.cellTable;
	}

	public void clearTable() {
		this.cellTable.setRowCount(0, true);
		this.cellTable.setRowData(0, new Vector<Entity>());
	}
}

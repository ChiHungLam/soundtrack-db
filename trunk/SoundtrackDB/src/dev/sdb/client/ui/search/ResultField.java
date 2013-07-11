package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;

public class ResultField extends Composite {

	interface ResultFieldUiBinder extends UiBinder<Widget, ResultField> {}
	private static ResultFieldUiBinder uiBinder = GWT.create(ResultFieldUiBinder.class);

	@UiField(provided = true) DataGrid<Entity> dataGrid;
	@UiField(provided = true) SimplePager pager;

	@UiField Label lastSearchLabel;
	@UiField Label selectionInfoLabel;
	@UiField HasVisibility tablePanel;

	public ResultField() {
		super();
		this.dataGrid = new DataGrid<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		this.pager.setDisplay(this.dataGrid);

		initWidget(uiBinder.createAndBindUi(this));

		setElementVisibility(-1);
	}

	public DataGrid<Entity> getDataGrid() {
		return this.dataGrid;
	}

	public void setLastSearchText(String text) {
		this.lastSearchLabel.setText(text);
	}

	public void setSelectionInfoText(String text) {
		this.selectionInfoLabel.setText(text);
	}

	public void setElementVisibility(int total) {
		//		this.tablePanel.setVisible(total > 0);
		this.lastSearchLabel.setVisible(total >= 0);
		this.selectionInfoLabel.setVisible(total >= 0);
	}
}

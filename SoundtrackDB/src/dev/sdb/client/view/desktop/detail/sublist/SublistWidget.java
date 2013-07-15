package dev.sdb.client.view.desktop.detail.sublist;

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

public class SublistWidget extends Composite {

	interface ReleaseListUiBinder extends UiBinder<Widget, SublistWidget> {}
	private static ReleaseListUiBinder uiBinder = GWT.create(ReleaseListUiBinder.class);

	@UiField(provided = true) DataGrid<Entity> dataGrid;
	@UiField(provided = true) SimplePager pager;

	@UiField Label selectionInfoLabel;
	@UiField HasVisibility tablePanel;

	public SublistWidget() {
		super();
		this.dataGrid = new DataGrid<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		initWidget(uiBinder.createAndBindUi(this));

		this.pager.setDisplay(this.dataGrid);
	}

	public DataGrid<Entity> getTable() {
		return this.dataGrid;
	}

	public void setSelectionInfoText(String text) {
		this.selectionInfoLabel.setText(text);
	}

	public void setElementVisibility(int total) {
		this.tablePanel.setVisible(total > 0);
		this.selectionInfoLabel.setVisible(total >= 0);
	}
}

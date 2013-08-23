package dev.sdb.client.view.desktop.detail.sublist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.model.entity.Entity;

public class SublistWidget extends Composite {

	interface SublistWidgetUiBinder extends UiBinder<Widget, SublistWidget> {}
	private static SublistWidgetUiBinder uiBinder = GWT.create(SublistWidgetUiBinder.class);

	@UiField(provided = true) CellTable<Entity> table;
	@UiField(provided = true) SimplePager pager;

	@UiField Label selectionInfoLabel;
	@UiField HasVisibility tablePanel;

	public SublistWidget() {
		super();
		this.table = new CellTable<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		initWidget(uiBinder.createAndBindUi(this));

		this.pager.setDisplay(this.table);
	}

	public CellTable<Entity> getTable() {
		return this.table;
	}

	public void setSelectionInfoText(String text) {
		this.selectionInfoLabel.setText(text);
	}

	public void setElementVisibility(int total) {
		this.tablePanel.setVisible(total > 0);
		this.selectionInfoLabel.setVisible(total >= 0);
	}
}

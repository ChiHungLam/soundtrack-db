package dev.sdb.client.controller;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.detail.SoundtrackDetailWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Soundtrack;

public class SoundtrackController extends AbstractSearchController {

	public SoundtrackController(SoundtrackDB sdb) {
		super(sdb, ControllerType.SOUNDTRACK, Flavor.SOUNDTRACK);
	}

	@Override protected void addSearchResultColumns(CellTable<Entity> table) {
		TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Soundtrack) entity).toString();
			}
		};

		// Make the columns sortable.
		column.setSortable(true);

		// Add the columns.
		table.addColumn(column, "Titel");

		table.setColumnWidth(column, 100.0, Unit.PCT);

		// We know that the data is sorted alphabetically by default.
		table.getColumnSortList().push(column);
	}

	@Override protected DetailWidget createDetailWidget() {
		return new SoundtrackDetailWidget();
	}
}

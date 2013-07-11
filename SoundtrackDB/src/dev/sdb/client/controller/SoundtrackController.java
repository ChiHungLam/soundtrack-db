package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.CellTable;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.detail.SoundtrackDetailWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackController extends AbstractDataController {

	public SoundtrackController(SoundtrackDB sdb) {
		super(sdb, ControllerType.SOUNDTRACK, Flavor.SOUNDTRACK);
	}

	@Override protected void addSearchResultColumns(CellTable<Entity> table) {
		addSoundtrackColumns(table);
	}

	@Override protected DetailWidget createDetailWidget() {
		return new SoundtrackDetailWidget(this);
	}
}

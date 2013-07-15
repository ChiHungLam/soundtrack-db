package dev.sdb.client.presenter;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.detail.DetailWidget;
import dev.sdb.client.ui.detail.SoundtrackDetailWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackController extends AbstractDataController {

	public SoundtrackController(SoundtrackDB sdb) {
		super(sdb, ControllerType.SOUNDTRACK, Flavor.SOUNDTRACK);
	}

	@Override protected void addSearchResultColumns(DataGrid<Entity> table) {
		addSoundtrackColumns(table, isSearchResultCompactView(), true);
	}

	@Override protected DetailWidget createDetailWidget() {
		return new SoundtrackDetailWidget(this);
	}
}

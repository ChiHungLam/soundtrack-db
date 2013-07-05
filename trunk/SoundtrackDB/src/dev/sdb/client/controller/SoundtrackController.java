package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.Column;
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

	@Override protected Column<Entity, ?> createRendererColumn() {
		TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Soundtrack) entity).toString();
			}
		};
		return column;
	}

	@Override protected DetailWidget createDetailWidget() {
		return new SoundtrackDetailWidget();
	}
}

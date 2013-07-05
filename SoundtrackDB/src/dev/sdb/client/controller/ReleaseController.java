package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleaseController extends AbstractSearchController {

	public ReleaseController(SoundtrackDB sdb) {
		super(sdb, ControllerType.RELEASE, Flavor.RELEASES);
	}

	@Override protected Column<Entity, ?> createRendererColumn() {
		final TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getTitle();
			}
		};
		return column;
	}

}

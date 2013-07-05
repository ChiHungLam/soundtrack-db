package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicController extends AbstractSearchController {

	public MusicController(SoundtrackDB sdb) {
		super(sdb, ControllerType.MUSIC, Flavor.MUSIC);
	}

	@Override protected Column<Entity, ?> createRendererColumn() {
		TextColumn<Entity> column = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getTitle();
			}
		};
		return column;
	}

}

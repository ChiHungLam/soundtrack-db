package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.user.client.ui.Composite;

import dev.sdb.shared.model.entity.Entity;

public abstract class MasterDataWidget extends Composite {

	public MasterDataWidget() {
		super();
	}

	public abstract void initEntity(Entity entity);
}

package dev.sdb.client.ui.detail.master;

import com.google.gwt.user.client.ui.Composite;

import dev.sdb.shared.model.entity.Entity;

public abstract class MasterDataWidget extends Composite {

	private Entity current;

	public MasterDataWidget() {
		super();
	}

	protected void setCurrentEntity(Entity current) {
		this.current = current;
	}

	public Entity getCurrentEntity() {
		return this.current;
	}

	public abstract void initEntity(Entity entity);
}

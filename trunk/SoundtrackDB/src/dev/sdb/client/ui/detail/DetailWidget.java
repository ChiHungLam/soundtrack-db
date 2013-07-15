package dev.sdb.client.ui.detail;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;

import dev.sdb.client.presenter.AbstractDataController;
import dev.sdb.client.ui.detail.master.MasterDataWidget;
import dev.sdb.shared.model.entity.Entity;

public abstract class DetailWidget extends Composite implements HasEnabled {

	private AbstractDataController controller;

	public DetailWidget(AbstractDataController controller) {
		super();
		this.controller = controller;
	}

	protected AbstractDataController getController() {
		return this.controller;
	}
	public Entity getCurrentEntity() {
		return getMasterDataWidget().getCurrentEntity();
	}

	protected abstract MasterDataWidget getMasterDataWidget();

	public abstract void initEntity(Entity entity);

	@Override public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}


}

package dev.sdb.client.view.desktop.detail;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;

import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.shared.model.entity.Entity;

public abstract class DetailWidget extends Composite implements HasEnabled {


	public DetailWidget() {
		super();
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

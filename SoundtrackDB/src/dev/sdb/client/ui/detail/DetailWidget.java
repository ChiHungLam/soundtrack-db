package dev.sdb.client.ui.detail;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.LongBox;

import dev.sdb.shared.model.entity.Entity;

public abstract class DetailWidget extends Composite implements HasEnabled {

	public DetailWidget() {
		super();
	}

	protected abstract LongBox getIdField();

	protected void setCurrentId(long currentId) {
		getIdField().setValue(Long.valueOf(currentId));
	}

	public long getCurrentId() {
		Long value = getIdField().getValue();
		return value == null ? -1 : value.longValue();
	}

	@Override public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	public abstract void initEntity(Entity entity);
}

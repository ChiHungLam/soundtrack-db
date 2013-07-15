package dev.sdb.client.view;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.shared.model.entity.Entity;

public interface DetailView extends IsWidget, HasEnabled {

	void initEntity(Entity entity);

}

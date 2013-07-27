package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;

import dev.sdb.shared.model.entity.Entity;

public interface CatalogTreeView extends IsWidget {

	public interface Presenter {
		void getCatalogLevelEntries(long parentId, AsyncDataProvider<Entity> dataProvider);
	}

}

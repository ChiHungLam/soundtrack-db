package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

public interface CatalogTreeView extends IsWidget {

	public interface Presenter {
		void getCatalogLevelEntries(long parentId, AsyncDataProvider<Entity> dataProvider);

		void getCatalogReleases(Catalog catalog, Range range, CatalogTreeView view);
	}

	void showResult(Result catalogResult);

}

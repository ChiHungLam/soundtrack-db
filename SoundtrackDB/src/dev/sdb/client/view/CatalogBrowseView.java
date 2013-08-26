package dev.sdb.client.view;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;

import dev.sdb.client.view.UiFactory.HtmlFactory;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

public interface CatalogBrowseView extends IsWidget {

	public interface Presenter {
		HtmlFactory getHtmlFactory();

		void getCatalogLevelEntries(long parentId, AsyncDataProvider<Entity> dataProvider);

		void getCatalogReleases(Catalog catalog, Range range, CatalogBrowseView view);
	}

	void showResult(Result catalogResult);

	CellTable<Entity> getReleaseTable();

}

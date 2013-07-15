package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public interface ReleaseSearchView extends IsWidget {

	public interface Presenter {
		void searchRelease(String term);
	}

	void setPresenter(Presenter presenter);

	void lockForSearch();

	void showSearchError(Throwable caught);

	void showSearchResult(Result searchResult);

	Range getRange();

	boolean isSortAscending();

	void setDataProvider(AsyncDataProvider<Entity> dataProvider);

	void setRange(int rangeStart, int rangeLength);

	void setSortAscending(boolean sortAscending);
}

package dev.sdb.client.view;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public interface QueryView extends IsWidget, HasText, HasEnabled {

	public interface Presenter {
		void onSearch(String term);

		void onBrowse(ContentPresenterType type, Entity entity);
	}

	void setPresenter(Presenter presenter);

	void showResult(String term, Result searchResult);

	void setDataProvider(AsyncDataProvider<Entity> dataProvider);

	void reset();

	void lockForSearch();

	boolean isSortAscending();

	Range getRange();
}
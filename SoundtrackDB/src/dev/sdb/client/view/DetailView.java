package dev.sdb.client.view;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;

import dev.sdb.client.view.UiFactory.HtmlFactory;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public interface DetailView extends IsWidget, HasEnabled {

	public interface Presenter {

		HtmlFactory getHtmlFactory();
		//void onBrowse(ContentPresenterType type, Entity entity);
	}

	void setPresenter(Presenter presenter);

	void initEntity(Entity entity);

	void setSublistDataProvider(AsyncDataProvider<Entity> dataProvider);

	void clearSublist();

	Range getSublistRange();

	void showSublistResult(String resultInfo, Result searchResult);

	CellTable<Entity> getSublistTable();
}

package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import dev.sdb.client.SoundtrackDB;
import dev.sdb.client.ui.search.AbstractQueryWidget;
import dev.sdb.client.ui.search.SearchEvent;
import dev.sdb.client.ui.search.SearchEventHandler;
import dev.sdb.client.ui.search.SearchField;
import dev.sdb.client.ui.search.SoundtrackQueryWidget;
import dev.sdb.client.ui.search.SoundtrackResultField;
import dev.sdb.shared.model.Entity;
import dev.sdb.shared.model.SearchScope;

public class SoundtrackController extends AbstractSearchController {

	public SoundtrackController(SoundtrackDB sdb) {
		super(sdb, SearchScope.SOUNDTRACK);

	}

	@Override public ControllerType getType() {
		return ControllerType.SOUNDTRACK;
	}

	@Override protected AbstractQueryWidget createQueryWidget(String term) {
		SoundtrackQueryWidget queryWidget = new SoundtrackQueryWidget();

		final SearchField search = queryWidget.getSearchField();
		search.setText(term);

		final SoundtrackResultField result = queryWidget.getResultField();

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				doSearchOnServer(search, result);
			}
		};

		result.init(dataProvider, VISIBLE_RANGE_LENGTH);

		//Add a handler to send the name to the server

		search.addSearchEventHandler(new SearchEventHandler() {
			/**
			 * Fired when the user clicks on the search button or hits enter in the search text field. Sends the name
			 * from the nameField to the server and waits for a response.
			 */
			@Override public void onSearch(SearchEvent event) {
				setLastSearchTerm(search.getText());
				final CellTable<Entity> table = result.getTable();
				table.setVisibleRange(0, VISIBLE_RANGE_LENGTH);
				doSearchOnServer(search, result);
			}
		});

		return queryWidget;
	}
}

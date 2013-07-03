package dev.sdb.client.controller;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import dev.sdb.client.ui.search.QueryResultField;
import dev.sdb.client.ui.search.QueryWidget;
import dev.sdb.client.ui.search.SearchEvent;
import dev.sdb.client.ui.search.SearchEventHandler;
import dev.sdb.client.ui.search.SearchField;
import dev.sdb.shared.model.SearchScope;
import dev.sdb.shared.model.SoundtrackContainer;

public class QueryController extends AbstractSearchController {

	public QueryController() {
		super(SearchScope.ALL);
		setUp();
	}

	@Override protected Widget createWidget() {
		return new QueryWidget();
	}

	@Override public QueryWidget getWidget(String state) {
		return (QueryWidget) super.getWidget(state);
	}


	private void setUp() {
		QueryWidget queryWidget = getWidget(null);

		final SearchField search = queryWidget.getSearchField();
		final QueryResultField result = queryWidget.getResultField();

		// Create a data provider.
		AsyncDataProvider<SoundtrackContainer> dataProvider = new AsyncDataProvider<SoundtrackContainer>() {
			@Override protected void onRangeChanged(HasData<SoundtrackContainer> display) {
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
				final CellTable<SoundtrackContainer> table = result.getTable();
				table.setVisibleRange(0, VISIBLE_RANGE_LENGTH);
				doSearchOnServer(search, result);
			}
		});
	}

}

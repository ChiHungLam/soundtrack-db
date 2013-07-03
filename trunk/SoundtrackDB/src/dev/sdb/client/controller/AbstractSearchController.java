package dev.sdb.client.controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;

import dev.sdb.client.service.SearchService;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.ui.search.AbstractResultField;
import dev.sdb.client.ui.search.SearchField;
import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;
import dev.sdb.shared.model.SoundtrackContainer;

public abstract class AbstractSearchController implements Controller {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SearchServiceAsync searchService = GWT.create(SearchService.class);

	private String lastSearchTerm;
	private SearchScope lastSearchScope;

	private Widget widget;

	public AbstractSearchController(SearchScope scope) {
		super();
		this.lastSearchScope = scope;
	}

	protected void setLastSearchTerm(String lastSearchTerm) {
		this.lastSearchTerm = lastSearchTerm;
	}

	protected abstract Widget createWidget();

	public Widget getWidget(String state) {
		if (this.widget == null)
			this.widget = createWidget();
		return this.widget;
	}



	protected void doSearchOnServer(final SearchField search, final AbstractResultField result) {

		//if there hasn't been a search before, cancel the action
		if (this.lastSearchTerm == null)
			return;

		final CellTable<SoundtrackContainer> table = result.getTable();
		final Range range = table.getVisibleRange();
		final ColumnSortInfo sortInfo = table.getColumnSortList().get(0);

		final SearchResultSort sort = new SearchResultSort(result.getSortType(), sortInfo.isAscending());

		//Disable search
		search.setEnabled(false);

		// Then, we send the input to the server.
		this.searchService.search(this.lastSearchTerm, this.lastSearchScope, range, sort, new AsyncCallback<SearchResult>() {

			public void onSuccess(SearchResult searchResult) {
				int total = searchResult.getTotalLength();

				table.setRowCount(total, true);
				table.setRowData(range.getStart(), searchResult.getResultChunk());
				result.setText(searchResult.getInfo());
				result.setElementVisibility(total);
				search.setEnabled(true);
			}

			public void onFailure(Throwable caught) {
				caught.printStackTrace();

				result.setElementVisibility(-1);

				// Create the popup dialog box
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("RPC - Fehler");
				//				dialogBox.setText("Remote Procedure Call");
				dialogBox.setAnimationEnabled(true);
				final Button closeButton = new Button("Close");
				// We can set the id of a widget by accessing its Element
				closeButton.getElement().setId("closeButton");

				final Label textToServerLabel = new Label();
				textToServerLabel.setText(AbstractSearchController.this.lastSearchTerm);

				final HTML serverResponseLabel = new HTML();
				serverResponseLabel.setText("");
				serverResponseLabel.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);

				VerticalPanel dialogVPanel = new VerticalPanel();
				dialogVPanel.addStyleName("dialogVPanel");
				dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
				dialogVPanel.add(textToServerLabel);
				dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
				dialogVPanel.add(serverResponseLabel);
				dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
				dialogVPanel.add(closeButton);
				dialogBox.setWidget(dialogVPanel);

				// Add a handler to close the DialogBox
				closeButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						dialogBox.hide();
						search.setEnabled(true);
					}
				});

				// Show the RPC error message to the user

				dialogBox.center();
				closeButton.setFocus(true);
			}
		});

	}
}

/**
 * 
 */
package dev.sdb.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.shared.SearchTermVerifier;


public class SearchField extends Composite implements HasEnabled, HasText, HasHandlers {

	interface SearchFieldUiBinder extends UiBinder<Widget, SearchField> {}
	private static SearchFieldUiBinder uiBinder = GWT.create(SearchFieldUiBinder.class);

	@UiField Button button;
	@UiField TextBox text;
	@UiField Label errorLabel;

	public SearchField() {
		this("");
	}

	public SearchField(String searchText) {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		if (searchText == null)
			searchText = "";

		setText(searchText);

		// Focus the cursor on the search field when the app loads
		this.text.setFocus(true);
		this.text.selectAll();

		this.button.addStyleName("sendButton");
		this.errorLabel.addStyleName("serverResponseLabelError");
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	/**
	 * Gets invoked when the default constructor is called
	 * and a string is provided in the ui.xml file.
	 */
	public String getText() {
		return this.text.getText();
	}

	@UiHandler("button")
	void onButtonClick(ClickEvent event) {
		prepareSearch();
	}

	@UiHandler("text")
	void onTextKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			prepareSearch();
		}
	}

	private void prepareSearch() {
		// Clear any previous error message
		this.errorLabel.setText("");

		// Get the input
		String searchText = this.text.getText();

		// Validate the input
		if (!SearchTermVerifier.isValidSearchTerm(searchText)) {
			// Show error message
			this.errorLabel.setText("Bitte mindestens " + SearchTermVerifier.SEARCH_TERM_MIN_LENGTH + " Zeichen angeben.");
			return; //cancel here!
		}

		// Fire search event
		fireEvent(new SearchEvent(searchText));
	}

	public HandlerRegistration addSearchEventHandler(SearchEventHandler handler) {
		return addHandler(handler, SearchEvent.TYPE);
    }

	@Override public boolean isEnabled() {
		return this.text.isEnabled();
	}

	@Override public void setEnabled(boolean enabled) {
		this.button.setEnabled(enabled);

		this.text.setEnabled(enabled);
		this.text.setFocus(true);
	}

}

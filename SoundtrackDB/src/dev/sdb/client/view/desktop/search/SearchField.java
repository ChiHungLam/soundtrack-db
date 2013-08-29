/**
 * 
 */
package dev.sdb.client.view.desktop.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.CssResource;
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

import dev.sdb.client.event.SearchEvent;
import dev.sdb.client.event.SearchEventHandler;
import dev.sdb.shared.SearchTermVerifier;


public class SearchField extends Composite implements HasEnabled, HasText, HasHandlers {

	interface SearchFieldUiBinder extends UiBinder<Widget, SearchField> {}
	private static SearchFieldUiBinder uiBinder = GWT.create(SearchFieldUiBinder.class);

	public interface Style extends CssResource {
		String error();

		//String gwtTextBox();
	}

	@UiField protected Style style;

	@UiField Button findButton;
	@UiField TextBox searchTerm;
	@UiField Label errorLabel;

	public SearchField() {
		this("");
	}

	public SearchField(String searchText) {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		this.searchTerm.removeStyleName("gwt-TextBox");//Otherwise appearance: searchfield; won't work
		if (searchText == null)
			searchText = "";

		setText(searchText);

		// Focus the cursor on the search field when the app loads
		this.searchTerm.setFocus(true);
		this.searchTerm.selectAll();
	}

	public void setText(String text) {
		this.searchTerm.setText(text);
	}

	/**
	 * Gets invoked when the default constructor is called
	 * and a string is provided in the ui.xml file.
	 */
	public String getText() {
		return this.searchTerm.getText();
	}

	@UiHandler("findButton")
	void onButtonClick(ClickEvent event) {
		prepareSearch();
	}

	@UiHandler("searchTerm")
	void onTextKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			prepareSearch();
		}
	}

	private void prepareSearch() {
		// Get the input
		String searchText = this.searchTerm.getText();

		// Validate the input
		if (!SearchTermVerifier.isValidSearchTerm(searchText)) {
			// Fire search event for invalid user entry 
			fireEvent(new SearchEvent(SearchEvent.Mode.INVALID, "Bitte mindestens " + SearchTermVerifier.SEARCH_TERM_MIN_LENGTH + " Zeichen angeben."));
		} else {
			// Fire search event
			fireEvent(new SearchEvent(SearchEvent.Mode.VALID, searchText));
		}
	}

	public HandlerRegistration addSearchEventHandler(SearchEventHandler handler) {
		return addHandler(handler, SearchEvent.TYPE);
    }

	@Override public boolean isEnabled() {
		return this.searchTerm.isEnabled();
	}

	@Override public void setEnabled(boolean enabled) {
		this.findButton.setEnabled(enabled);
		this.searchTerm.setEnabled(enabled);

		this.searchTerm.setFocus(true);
	}

	protected void showErrorDisplay(String message) {
		this.errorLabel.setText(message);
		this.errorLabel.addStyleName(this.style.error());
	}

	protected void clearErrorDisplay() {
		this.errorLabel.setText("");
		this.errorLabel.removeStyleName(this.style.error());
	}

}

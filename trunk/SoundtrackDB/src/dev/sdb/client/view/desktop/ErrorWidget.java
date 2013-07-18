package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.ErrorView;

public class ErrorWidget extends Composite implements ErrorView {

	interface ErrorWidgetUiBinder extends UiBinder<Widget, ErrorWidget> {}
	private static ErrorWidgetUiBinder uiBinder = GWT.create(ErrorWidgetUiBinder.class);

	public interface ErrorStyle extends CssResource {
		String displayed();
	}

	@UiField protected ErrorStyle style;

	@UiField FlowPanel glassPanel;
	@UiField FlowPanel dialogPanel;

	@UiField Label errorTitle;
	@UiField HTML errorMessage;
	@UiField Button closeButton;

	@SuppressWarnings("unused") private Presenter presenter;

	public ErrorWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override public HasText getTitleWidget() {
		return this.errorTitle;
	}

	@Override public HasHTML getMessageWidget() {
		return this.errorMessage;
	}

	@Override public void show() {
		this.glassPanel.addStyleName(this.style.displayed());
		this.dialogPanel.addStyleName(this.style.displayed());
	}

	@Override public void hide() {
		this.glassPanel.removeStyleName(this.style.displayed());
		this.dialogPanel.removeStyleName(this.style.displayed());
	}
	@UiHandler("closeButton")
	void onCloseButtonClick(ClickEvent event) {
		hide();
	}
}

package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.UiFactory.HtmlFactory;

public abstract class AbstractContentPresenter implements ContentPresenter {

	private final ClientFactory clientFactory;
	private final ContentPresenterType type;

	public AbstractContentPresenter(ClientFactory clientFactory, ContentPresenterType type) {
		super();
		this.clientFactory = clientFactory;
		this.type = type;
	}

	protected ClientFactory getClientFactory() {
		return this.clientFactory;
	}

	public HtmlFactory getHtmlFactory() {
		return getClientFactory().getUi().getHtmlFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dev.sdb.client.presenter.ContentPresenter#getType()
	 */
	@Override public ContentPresenterType getType() {
		return this.type;
	}

}

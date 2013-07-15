package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see dev.sdb.client.presenter.ContentPresenter#getType()
	 */
	@Override public ContentPresenterType getType() {
		return this.type;
	}

}

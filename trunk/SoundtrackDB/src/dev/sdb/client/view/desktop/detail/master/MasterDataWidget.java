package dev.sdb.client.view.desktop.detail.master;

import com.google.gwt.user.client.ui.Composite;

import dev.sdb.client.view.DetailView;
import dev.sdb.shared.model.entity.Entity;

public abstract class MasterDataWidget extends Composite {

	private DetailView.Presenter presenter;

	public MasterDataWidget() {
		super();
	}

	public abstract void initEntity(Entity entity);

	public void setPresenter(DetailView.Presenter presenter) {
		this.presenter = presenter;
	}

	protected DetailView.Presenter getPresenter() {
		return this.presenter;
	}
}

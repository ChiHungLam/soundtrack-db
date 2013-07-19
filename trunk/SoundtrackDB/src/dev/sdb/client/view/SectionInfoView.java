package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.presenter.ContentPresenterType;

public interface SectionInfoView extends IsWidget {

	public interface Presenter {
		void switchToArea(ContentPresenterType type);
	}

	void setPresenter(Presenter presenter);

	void display(ContentPresenterType type);
}

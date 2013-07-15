package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;

import dev.sdb.client.presenter.ContentPresenter;

public interface HomeView extends IsWidget {

	void setPresenter(Presenter presenter);

	public interface Presenter extends ContentPresenter {
		//		ControllerType getType();
		//
		//		Widget getWidget(String state);
	}
}

package dev.sdb.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface HomeView extends IsWidget {


	public interface Presenter {
		//		ControllerType getType();
		//
		//		Widget getWidget(String state);
	}

	void setPresenter(Presenter presenter);
}

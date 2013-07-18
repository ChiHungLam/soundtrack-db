package dev.sdb.client.view;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface ErrorView extends IsWidget {



	public interface Presenter {
		void showError(String title, String message, Throwable caught);
	}

	void setPresenter(Presenter presenter);

	HasText getTitleWidget();
	HasHTML getMessageWidget();

	void show();

	void hide();
}

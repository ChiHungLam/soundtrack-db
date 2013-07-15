package dev.sdb.client.view;

import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;

public interface ReleaseDetailView extends DetailView {

	public interface Presenter {
		void getSequenceListFromServer(ReleaseDetailView view);
	}

	void setPresenter(Presenter presenter);

	SublistWidget getSublist();
}

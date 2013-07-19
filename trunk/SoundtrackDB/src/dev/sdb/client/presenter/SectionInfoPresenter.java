package dev.sdb.client.presenter;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.SectionInfoView;

public class SectionInfoPresenter implements SectionInfoView.Presenter {

	private ClientFactory clientFactory;

	//	private int lastKeyCode = 36;

	public SectionInfoPresenter(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	public void switchToArea(ContentPresenterType type) {
		//		this.lastKeyCode++;
		//		if (this.lastKeyCode > 40)
		//			this.lastKeyCode = 37;

		SectionInfoView view = this.clientFactory.getUi().getSectionInfoView();
		view.display(type);
	}

}

package dev.sdb.client.presenter;

import com.google.gwt.user.cellview.client.DataGrid;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.SoundtrackDetailView;
import dev.sdb.client.view.SoundtrackSearchView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackController extends AbstractDataController implements SoundtrackSearchView.Presenter,
		SoundtrackDetailView.Presenter {

	public SoundtrackController(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.SOUNDTRACK, Flavor.SOUNDTRACK);
	}

	@Override protected void addSearchResultColumns(DataGrid<Entity> table) {
		addSoundtrackColumns(table, isSearchResultCompactView(), true);
	}

	@Override protected DetailView createDetailWidget() {
		SoundtrackDetailView view = getClientFactory().getUi().getSoundtrackDetailView();
		view.setPresenter(this);
		return view;
	}

}

package dev.sdb.client.presenter;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.QueryView;
import dev.sdb.client.view.SoundtrackDetailView;
import dev.sdb.client.view.SoundtrackQueryView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackPresenter extends AbstractBrowsePresenter implements SoundtrackQueryView.Presenter,
		SoundtrackDetailView.Presenter {

	public SoundtrackPresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.SOUNDTRACK, Flavor.SOUNDTRACK);
	}

	@Override protected DetailView createDetailView() {
		// Create the query widget instance
		final SoundtrackDetailView view = getClientFactory().getUi().getSoundtrackDetailView();
		view.setPresenter(this);
		return view;
	}

	protected QueryView createQueryView(String term) {
		// Create the query widget instance
		final SoundtrackQueryView view = getClientFactory().getUi().getSoundtrackQueryView();
		view.setPresenter(this);

		// Set the search term
		view.setText(term);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSearchFromServer(view);
			}
		};

		view.setDataProvider(dataProvider);

		return view;
	}
}

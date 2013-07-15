package dev.sdb.client.presenter;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.view.SearchView;
import dev.sdb.client.view.SoundtrackSearchView;
import dev.sdb.client.view.desktop.detail.DetailWidget;
import dev.sdb.client.view.desktop.detail.SoundtrackDetailWidget;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;

public class SoundtrackPresenter extends AbstractBrowsePresenter implements SoundtrackSearchView.Presenter {

	public SoundtrackPresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.SOUNDTRACK, Flavor.SOUNDTRACK);
	}

	@Override protected DetailWidget createDetailWidget() {
		return new SoundtrackDetailWidget(this);
	}

	protected SearchView createQueryWidget(String term) {
		// Create the query widget instance
		final SoundtrackSearchView queryWidget = getClientFactory().getUi().getSoundtrackSearchView();
		queryWidget.setPresenter(this);

		// Set the search term
		queryWidget.setText(term);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSearchFromServer(queryWidget);
			}
		};

		queryWidget.setDataProvider(dataProvider);

		return queryWidget;
	}
}

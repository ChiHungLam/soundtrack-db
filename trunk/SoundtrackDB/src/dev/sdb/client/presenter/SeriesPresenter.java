package dev.sdb.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.QueryView;
import dev.sdb.client.view.SeriesDetailView;
import dev.sdb.client.view.SeriesQueryView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Series;

public class SeriesPresenter extends AbstractBrowsePresenter implements SeriesQueryView.Presenter,
	SeriesDetailView.Presenter {

	public SeriesPresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.SERIES, Flavor.SERIES);
	}


	@Override protected DetailView createDetailView() {
		final SeriesDetailView view = getClientFactory().getUi().getSeriesDetailView();
		view.setPresenter(this);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSeriesReleaseListFromServer(view);
			}
		};
		view.setSublistDataProvider(dataProvider);

		return view;
	}

	public void getSeriesReleaseListFromServer(final SeriesDetailView view) {

		Series music = (Series) getCurrentDetailEntity();
		if (music == null) {
			view.clearSublist();
			return;
		}

		final long id = music.getId();

		//if there's no id, cancel the action
		if (id <= 0) {
			view.clearSublist();
			return;
		}

		final Range range = view.getSublistRange();

		SearchServiceAsync service = getClientFactory().getSearchService();

		// Then, we send the input to the server.
		service.getSeriesReleaseList(id, range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				int total = searchResult.getTotalLength();

				String resultInfo = "";
				if (total == 0) {
					resultInfo = "Für diese Musik sind keine Veröffentlichungen bekannt.";
				} else if (total > 0) {
					resultInfo = "Für diese Musik " + (total == 1 ? "ist 1 Veröffentlichung" : ("sind " + total + " Veröffentlichungen")) + " bekannt.";
				}

				view.showSublistResult(resultInfo, searchResult);
			}

			public void onFailure(Throwable caught) {
				handleRpcError("Release list for [" + Flavor.MUSIC.name() + "] id=" + id, caught);
				view.clearSublist();
			}
		});

	}


	protected QueryView createQueryView(String term) {
		// Create the query widget instance
		final SeriesQueryView queryWidget = getClientFactory().getUi().getSeriesQueryView();
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
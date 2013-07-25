package dev.sdb.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.MusicDetailView;
import dev.sdb.client.view.MusicQueryView;
import dev.sdb.client.view.QueryView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicPresenter extends AbstractSearchablePresenter implements MusicQueryView.Presenter,
		MusicDetailView.Presenter {

	public MusicPresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.MUSIC, Flavor.MUSIC);
	}


	@Override protected DetailView createDetailView() {
		final MusicDetailView view = getClientFactory().getUi().getMusicDetailView();
		view.setPresenter(this);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getMusicReleaseListFromServer(view);
			}
		};
		view.setSublistDataProvider(dataProvider);

		return view;
	}

	public void getMusicReleaseListFromServer(final MusicDetailView view) {

		Music music = (Music) getCurrentDetailEntity();
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
		service.getMusicReleaseList(id, range, new AsyncCallback<Result>() {

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
		final MusicQueryView queryWidget = getClientFactory().getUi().getMusicQueryView();
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

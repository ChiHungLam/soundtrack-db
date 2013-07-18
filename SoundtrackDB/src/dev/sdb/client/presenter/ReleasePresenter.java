package dev.sdb.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.service.SearchServiceAsync;
import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.QueryView;
import dev.sdb.client.view.ReleaseDetailView;
import dev.sdb.client.view.ReleaseQueryView;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Release;

public class ReleasePresenter extends AbstractBrowsePresenter implements ReleaseQueryView.Presenter,
		ReleaseDetailView.Presenter {

	public ReleasePresenter(ClientFactory clientFactory) {
		super(clientFactory, ContentPresenterType.RELEASE, Flavor.RELEASES);
	}

	protected QueryView createQueryView(String term) {
		// Create the query widget instance
		final ReleaseQueryView view = getClientFactory().getUi().getReleaseQueryView();
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

	@Override protected DetailView createDetailView() {
		final ReleaseDetailView view = getClientFactory().getUi().getReleaseDetailView();
		view.setPresenter(this);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				getSequenceListFromServer(view);
			}
		};

		view.setSublistDataProvider(dataProvider);

		return view;
	}

	public void getSequenceListFromServer(final ReleaseDetailView view) {

		Release release = (Release) getCurrentDetailEntity();
		if (release == null) {
			view.clearSublist();
			return;
		}

		final long audioId = release.getAudioId();

		//if there's no id, cancel the action
		if (audioId <= 0) {
			view.clearSublist();
			return;
		}

		final Range range = view.getSublistRange();

		SearchServiceAsync service = getClientFactory().getSearchService();

		// Then, we send the input to the server.
		service.getReleaseSoundtrackList(audioId, range, new AsyncCallback<Result>() {

			public void onSuccess(Result searchResult) {
				int total = searchResult.getTotalLength();

				String resultInfo = "";
				if (total == 0) {
					resultInfo = "Für diese Veröffentlichung existieren keine Sequenzen.";
				} else if (total > 0) {
					resultInfo = "Für diese Veröffentlichung " + (total == 1 ? "existiert 1 Sequenz" : ("existieren " + total + " Sequenzen")) + ".";
				}

				view.showSublistResult(resultInfo, searchResult);
			}

			public void onFailure(Throwable caught) {
				handleRpcError("Sequence list for [" + Flavor.RELEASES.name() + "] audioId=" + audioId, caught);
				view.clearSublist();
			}
		});
	}



}

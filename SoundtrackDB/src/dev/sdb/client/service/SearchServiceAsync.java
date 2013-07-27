package dev.sdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

/**
 * The async counterpart of <code>SearchService</code>.
 */
public interface SearchServiceAsync {

	void search(Flavor flavor, String term, Range range, boolean ascending, AsyncCallback<Result> callback);

	void get(Flavor flavor, long id, AsyncCallback<Entity> callback);

	void getReleaseSoundtrackList(long id, Range range, AsyncCallback<Result> asyncCallback);

	void getMusicReleaseList(long id, Range range, AsyncCallback<Result> asyncCallback);

	void getSeriesReleaseList(long id, Range range, AsyncCallback<Result> asyncCallback);

	void getCatalogList(long parentId, AsyncCallback<Result> asyncCallback);

}

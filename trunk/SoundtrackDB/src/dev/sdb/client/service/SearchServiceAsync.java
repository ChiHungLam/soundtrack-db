package dev.sdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.SearchResult;

/**
 * The async counterpart of <code>SearchService</code>.
 */
public interface SearchServiceAsync {

	void search(String term, Flavor flavor, Range range, boolean ascending, AsyncCallback<SearchResult> callback);

}

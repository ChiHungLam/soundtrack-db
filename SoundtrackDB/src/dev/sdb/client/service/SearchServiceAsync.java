package dev.sdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.SearchResult;
import dev.sdb.shared.model.db.SearchScope;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SearchServiceAsync {

	void search(String term, SearchScope scope, Range range, boolean ascending, AsyncCallback<SearchResult> callback);

}

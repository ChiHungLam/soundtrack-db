package dev.sdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SearchServiceAsync {
	void search(String term, SearchScope scope, Range range, SearchResultSort sort, AsyncCallback<SearchResult> callback);

	//	void searchMusic(String term, Range range, SearchResultSort sort, AsyncCallback<SearchResult> callback);
	//
	//	void searchReleases(String term, Range range, SearchResultSort sort, AsyncCallback<SearchResult> callback);
}

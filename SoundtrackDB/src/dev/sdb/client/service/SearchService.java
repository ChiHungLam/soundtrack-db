package dev.sdb.client.service;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("search") public interface SearchService extends RemoteService {

	SearchResult search(String term, SearchScope scope, Range range, SearchResultSort sort) throws IllegalArgumentException, IOException;

}

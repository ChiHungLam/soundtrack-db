package dev.sdb.client.service;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.SearchResult;
import dev.sdb.shared.model.entity.Entity;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("search") public interface SearchService extends RemoteService {

	SearchResult search(Flavor flavor, String term, Range range, boolean ascending) throws IllegalArgumentException, IOException;

	Entity get(Flavor flavor, long id) throws IllegalArgumentException, IOException;
}

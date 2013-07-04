package dev.sdb.server;

import java.io.IOException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.Range;

import dev.sdb.client.service.SearchService;
import dev.sdb.server.db.Database;
import dev.sdb.shared.FieldVerifier;
import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial") public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {

	private static final Database db;
	
	static {
		try {
			db = new Database();
		} catch (Exception e) {
			throw new IllegalStateException("Database creation failed.", e);
		}
	}

	public SearchResult search(String term, final SearchScope scope, final Range range, final SearchResultSort sort) throws IllegalArgumentException, IOException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidSearchTerm(term)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Bitte mindestens " + FieldVerifier.SEARCH_TERM_MIN_LENGTH + " Zeichen angeben.");
		}
		if (scope == null) {
			throw new IllegalArgumentException("Bitte einen Suchbereich auswählen.");
		}

		// Escape data from the client to avoid cross-site script vulnerabilities.
		term = escapeHtml(term);

		//		MockupDatabase db = new MockupDatabase();


		SearchResult searchResult = db.querySoundtrackContainer(term, scope, range, sort);

		return searchResult;
		}

	//	public SearchResult searchMusic(String term, final Range range, final SearchResultSort sort) throws IllegalArgumentException, IOException {
	//		// Verify that the input is valid. 
	//		if (!FieldVerifier.isValidSearchTerm(term)) {
	//			// If the input is not valid, throw an IllegalArgumentException back to
	//			// the client.
	//			throw new IllegalArgumentException("Bitte mindestens " + FieldVerifier.SEARCH_TERM_MIN_LENGTH + " Zeichen angeben.");
	//		}
	//
	//		// Escape data from the client to avoid cross-site script vulnerabilities.
	//		term = escapeHtml(term);
	//
	//		//		MockupDatabase db = new MockupDatabase();
	//		Database db = new Database();
	//
	//		SearchResult searchResult = db.querySoundtrackContainer(term, SearchScope.MUSIC_ONLY, range, sort);
	//
	//		return searchResult;
	//	}
	//
	//	public SearchResult searchReleases(String term, final Range range, final SearchResultSort sort) throws IllegalArgumentException, IOException {
	//		// Verify that the input is valid. 
	//		if (!FieldVerifier.isValidSearchTerm(term)) {
	//			// If the input is not valid, throw an IllegalArgumentException back to
	//			// the client.
	//			throw new IllegalArgumentException("Bitte mindestens " + FieldVerifier.SEARCH_TERM_MIN_LENGTH + " Zeichen angeben.");
	//		}
	//
	//		// Escape data from the client to avoid cross-site script vulnerabilities.
	//		term = escapeHtml(term);
	//
	//		//		MockupDatabase db = new MockupDatabase();
	//		Database db = new Database();
	//
	//		SearchResult searchResult = db.querySoundtrackContainer(term, SearchScope.RELEASES_ONLY, range, sort);
	//
	//		return searchResult;
	//	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	protected String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;");
	}
}

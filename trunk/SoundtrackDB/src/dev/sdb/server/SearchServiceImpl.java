package dev.sdb.server;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.Range;

import dev.sdb.client.service.SearchService;
import dev.sdb.server.db.SdbManager;
import dev.sdb.server.db.SqlServer;
import dev.sdb.shared.FieldVerifier;
import dev.sdb.shared.model.Entity;
import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial") public class SearchServiceImpl extends RemoteServiceServlet implements
		SearchService {

	private static final SqlServer sqlServer;
	
	static {
		SqlServer server = null;
		try {
			Properties properties = new Properties();
			properties.loadFromXML(SqlServer.class.getResourceAsStream("properties.xml"));

			server = new SqlServer();
			server.init(properties);

		} catch (Exception e) {
			server = null;
			throw new IllegalStateException("Sql server connection failed.", e);
		} finally {
			sqlServer = server;
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

		// Perform the actual query
		SearchResult searchResult = query(term, scope, range, sort);

		return searchResult;
	}

	private SearchResult query(String term, SearchScope scope, Range range, final SearchResultSort sort) throws IOException, IllegalArgumentException {
		SdbManager manager = new SdbManager(sqlServer);

		try {

			List<Entity> result = new Vector<Entity>();

			int count;

			switch (scope) {
			case RELEASES:
				manager.openReleases(range, sort);
				count = manager.countReleases(term);
				manager.queryReleases(result, term);

				break;
			case MUSIC:
				manager.openMusic(range, sort);
				count = manager.countMusic(term);
				manager.queryMusic(result, term);

				break;
			case SOUNDTRACK:
				manager.openSoundtracks(range, sort);
				count = manager.countSoundtracks(term);
				manager.querySoundtracks(result, term);

				break;

			default:
				result.clear();
				throw new IllegalArgumentException("illegal scope: " + scope);
			}

			String info = "Suchergebnis für: '" + term + "'.";
			if (count > 0)
				info += "Gefundene Einträge: " + count;

			return new SearchResult(info, result, count);

		} finally {
			manager.close();
		}
	}

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

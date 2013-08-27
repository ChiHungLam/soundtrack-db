package dev.sdb.server;

import java.io.IOException;
import java.util.Properties;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.Range;

import dev.sdb.client.service.SearchService;
import dev.sdb.server.db.Database;
import dev.sdb.server.db.SqlServer;
import dev.sdb.server.db.impl.ComplexDatabase;
import dev.sdb.shared.SearchTermVerifier;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

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

	private Database getDatabase() {
		return new ComplexDatabase(sqlServer);
	}

	@Override public Entity get(Flavor flavor, long id) throws IllegalArgumentException, IOException {
		assert (flavor != null);
		assert (id > 0);

		Database database = getDatabase();
		database.open();

		try {
			return database.getEntity(flavor, id);
		} finally {
			database.close();
		}
	}

	@Override public Result getMusicReleaseList(long id, Range range) throws IllegalArgumentException, IOException {
		assert (id > 0);
		assert (range != null);

		Database database = getDatabase();
		database.open();

		try {
			return database.getMusicReleaseList(id, range);
		} finally {
			database.close();
		}
	}

	@Override public Result getSeriesReleaseList(long id, Range range) throws IllegalArgumentException, IOException {
		assert (id > 0);
		assert (range != null);

		Database database = getDatabase();
		database.open();

		try {
			return database.getSeriesReleaseList(id, range);
		} finally {
			database.close();
		}
	}

	@Override public Result getReleaseSoundtrackList(long id) throws IllegalArgumentException, IOException {
		assert (id > 0);

		Database database = getDatabase();
		database.open();

		try {
			return database.getReleaseSoundtrackList(id);
		} finally {
			database.close();
		}
	}

	@Override public Result getCatalogReleaseList(long catalogId, Range range) throws IllegalArgumentException, IOException {
		assert (catalogId >= 0);

		Database database = getDatabase();
		database.open();

		try {
			return database.getCatalogReleaseList(catalogId, range);
		} finally {
			database.close();
		}
	}

	@Override public Result getCatalogList(long parentCatalogId) throws IllegalArgumentException, IOException {
		assert (parentCatalogId >= 0);

		Database database = getDatabase();
		database.open();

		try {
			//database.repairCatalogChildInfo();

			database.getCatalogList(parentCatalogId);

			return database.getCatalogList(parentCatalogId);
		} finally {
			database.close();
		}
	}



	@Override public Result search(Flavor flavor, String term, Range range, boolean ascending) throws IllegalArgumentException, IOException {
		assert (flavor != null);
		assert (range != null);

		// Verify that the input is valid. 
		if (!SearchTermVerifier.isValidSearchTerm(term)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Bitte mindestens " + SearchTermVerifier.SEARCH_TERM_MIN_LENGTH + " Zeichen angeben.");
		}

		// Escape data from the client to avoid cross-site script vulnerabilities.
		term = escapeHtml(term);

		Database database = getDatabase();
		database.open();

		try {
			return database.query(flavor, term, range, ascending);
		} finally {
			database.close();
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

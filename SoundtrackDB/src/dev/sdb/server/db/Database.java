package dev.sdb.server.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.Music;
import dev.sdb.shared.model.Release;
import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;
import dev.sdb.shared.model.SoundtrackContainer;


public class Database {

	private SqlServer sqlServer;

	public Database() throws IOException, IllegalArgumentException {
		super();
		Properties properties = loadProperties();

		this.sqlServer = new SqlServer();
		this.sqlServer.init(properties);
	}

	private Properties loadProperties() throws IOException {

		//		String propertiesPath = ;

		Properties properties = new Properties();
		properties.loadFromXML(Database.class.getResourceAsStream("properties.xml"));
		return properties;
	}

	public SearchResult querySoundtrackContainer(String term, SearchScope scope, Range range, final SearchResultSort sort) throws IOException, IllegalArgumentException {

		String info = "Letzte Suche: nach '" + term + "' im Bereich '" + scope.name() + "'.";
		List<SoundtrackContainer> result = new Vector<SoundtrackContainer>();

		int count;

		switch (scope) {
		case RELEASES:
			count = countReleases(term);
			queryReleases(result, term, range, sort);

			break;
		case MUSIC:
			count = countMusic(term);
			queryMusic(result, term, range, sort);

			break;
		case SOUNDTRACK:
			count = 0;//countSoundtracks(term);
			//querySoundtracks(result, term, range, sort);

			break;

		default:
			result.clear();
			throw new IllegalArgumentException("illegal scope: " + scope);
		}


		return new SearchResult(info, result, count);
	}

	//	private List<SoundtrackContainer> prepareCombinedResult(List<SoundtrackContainer> result, Range range, final SearchResultSort sort) {
	//		final Comparator<SoundtrackContainer> titleComparator = new Comparator<SoundtrackContainer>() {
	//			public int compare(SoundtrackContainer o1, SoundtrackContainer o2) {
	//				if (o1 == o2) {
	//					return 0;
	//				}
	//
	//				// Compare the name columns.
	//				int diff = -1;
	//				if (o1 != null) {
	//					diff = (o2 != null) ? o1.getTitle().compareTo(o2.getTitle()) : 1;
	//				}
	//				return sort.isAscending() ? diff : -diff;
	//			}
	//		};
	//
	//		Comparator<SoundtrackContainer> comparator;
	//		switch (sort.getType()) {
	//		case TITLE:
	//			comparator = titleComparator;
	//			break;
	//		case TYPE:
	//			final Comparator<SoundtrackContainer> typeComparator = new Comparator<SoundtrackContainer>() {
	//				public int compare(SoundtrackContainer o1, SoundtrackContainer o2) {
	//					int result;
	//
	//					if (o1 == o2) {
	//						result = 0;
	//					} else {
	//						int diff = -1;
	//						if (o1 != null) {
	//							diff = (o2 != null) ? o1.getType().compareTo(o2.getType()) : 1;
	//						}
	//						result = sort.isAscending() ? diff : -diff;
	//					}
	//
	//					if (result != 0)
	//						return result;
	//					return titleComparator.compare(o1, o2);
	//				}
	//			};
	//			comparator = typeComparator;
	//			break;
	//
	//		default:
	//			comparator = titleComparator;
	//			break;
	//		}
	//
	//		Collections.sort(result, comparator);
	//
	//		int start = range.getStart();
	//		int length = range.getLength();
	//		int end = start + length;
	//
	//		if (length > result.size())
	//			return result;
	//		else
	//			return new Vector<SoundtrackContainer>(result.subList(start, end));
	//
	//	}

	private int countMusic(String term) throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT( * ) AS `rows` FROM `recording` WHERE `rec_title` LIKE ?";
			ps = this.sqlServer.getConnection().prepareStatement(sql);
			ps.setString(1, "%" + term + "%");
			rs = ps.executeQuery();
			if (!rs.next())
				return 0;

			return rs.getInt("rows");

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps = null;
			}
		}
	}

	private void queryMusic(List<SoundtrackContainer> result, String term, Range range, SearchResultSort sort) throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT * FROM `recording` WHERE `rec_title` LIKE ?";

			if (sort != null) {
				sql += " ORDER BY `recording`.`rec_title` " + (sort.isAscending() ? "ASC" : "DESC");
			}

			if (range != null) {
				int start = range.getStart();
				int length = range.getLength();
				sql += " LIMIT " + start + " , " + length;
			}

			ps = this.sqlServer.getConnection().prepareStatement(sql);
			ps.setString(1, "%" + term + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				result.add(new Music(rs.getString("rec_title")));
			}

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps = null;
			}
		}



	}

	private int countReleases(String term) throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT COUNT( * ) AS `rows` FROM `production` WHERE `prod_res_title` LIKE ?";
			ps = this.sqlServer.getConnection().prepareStatement(sql);
			ps.setString(1, "%" + term + "%");
			rs = ps.executeQuery();
			if (!rs.next())
				return 0;

			return rs.getInt("rows");

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps = null;
			}
		}
	}

	private void queryReleases(List<SoundtrackContainer> result, String term, Range range, SearchResultSort sort) throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM `production` WHERE `prod_res_title` LIKE ?";

			if (sort != null) {
				sql += " ORDER BY `production`.`prod_res_title` " + (sort.isAscending() ? "ASC" : "DESC");
			}

			if (range != null) {
				int start = range.getStart();
				int length = range.getLength();
				sql += " LIMIT " + start + " , " + length;
			}
			ps = this.sqlServer.getConnection().prepareStatement(sql);
			ps.setString(1, "%" + term + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				result.add(new Release(rs.getString("prod_res_title")));
			}

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps = null;
			}
		}
	}
}

package dev.sdb.server.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.Entity;
import dev.sdb.shared.model.Music;
import dev.sdb.shared.model.Release;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.Soundtrack;

public class SdbManager extends SqlManager {

	private PreparedStatement musicCountPS;
	private PreparedStatement releaseCountPS;
	private PreparedStatement soundtrackCountPS;

	private PreparedStatement musicListPS;
	private PreparedStatement releaseListPS;
	private PreparedStatement soundtrackListPS;

	public SdbManager(SqlServer sqlServer) {
		super(sqlServer);
	}

	@Override public void open() throws IOException {
		openConnection();
	}

	public void openReleases(Range range, SearchResultSort sort) throws IOException {
		open();

		try {
			this.releaseCountPS = createReleaseCountPS();
			this.releaseListPS = createReleaseListPS(range, sort);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	public void openMusic(Range range, SearchResultSort sort) throws IOException {
		open();

		try {
			this.musicCountPS = createMusicCountPS();
			this.musicListPS = createMusicListPS(range, sort);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	public void openSoundtracks(Range range, SearchResultSort sort) throws IOException {
		open();

		try {
			this.soundtrackCountPS = createSoundtrackCountPS();
			this.soundtrackListPS = createSoundtrackListPS(range, sort);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	@Override public void close() {
		closeStatement(this.musicCountPS);
		closeStatement(this.releaseCountPS);
		closeStatement(this.soundtrackCountPS);

		closeStatement(this.musicListPS);
		closeStatement(this.releaseListPS);
		closeStatement(this.soundtrackListPS);

		closeConnection();

		this.musicCountPS = null;
		this.releaseCountPS = null;
		this.soundtrackCountPS = null;

		this.musicListPS = null;
		this.releaseListPS = null;
		this.soundtrackListPS = null;
	}



	public int countMusic(String term) throws IOException {
		ResultSet rs = null;

		try {

			this.musicCountPS.clearParameters();
			this.musicCountPS.setString(1, "%" + term + "%");

			rs = this.musicCountPS.executeQuery();
			if (!rs.next())
				return 0;

			return rs.getInt("rows");

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}
	}


	public void queryMusic(List<Entity> result, String term) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";
			

			this.musicListPS.clearParameters();
			this.musicListPS.setString(1, wildcardedTerm);

			rs = this.musicListPS.executeQuery();

			while (rs.next()) {
				long id = rs.getLong("rec_id");
				String title = rs.getString("rec_title");
				result.add(new Music(id, title));
			}

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}

	}

	public int countSoundtracks(String term) throws IOException {
		ResultSet rs = null;

		try {

			this.soundtrackCountPS.clearParameters();
			this.soundtrackCountPS.setString(1, "%" + term + "%");

			rs = this.soundtrackCountPS.executeQuery();
			if (!rs.next())
				return 0;

			return rs.getInt("rows");

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}
	}

	public int countReleases(String term) throws IOException {
		ResultSet rs = null;

		try {

			this.releaseCountPS.clearParameters();
			this.releaseCountPS.setString(1, "%" + term + "%");

			rs = this.releaseCountPS.executeQuery();
			if (!rs.next())
				return 0;

			return rs.getInt("rows");

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}
	}

	public void querySoundtracks(List<Entity> result, String term) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			this.soundtrackListPS.clearParameters();
			this.soundtrackListPS.setString(1, wildcardedTerm);

			rs = this.soundtrackListPS.executeQuery();

			while (rs.next()) {
				long id = rs.getLong("stk_id");
				//				String title = rs.getString("prod_res_title");
				result.add(new Soundtrack(id));
			}

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			closeResultSet(rs);
		}
	}

	public void queryReleases(List<Entity> result, String term) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			this.releaseListPS.clearParameters();
			this.releaseListPS.setString(1, wildcardedTerm);

			rs = this.releaseListPS.executeQuery();

			while (rs.next()) {
				long id = rs.getLong("prod_id");
				String title = rs.getString("prod_res_title");
				result.add(new Release(id, title));
			}

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			closeResultSet(rs);
		}
	}

	private PreparedStatement createMusicCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` FROM `recording` WHERE `rec_title` LIKE ?";
		return getStatement(sql);
	}

	private PreparedStatement createReleaseCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` FROM `production` WHERE `prod_res_title` LIKE ?";
		return getStatement(sql);
	}

	private PreparedStatement createSoundtrackCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` FROM `soundtrack` WHERE `stk_interna` LIKE ?";
		return getStatement(sql);
	}

	private PreparedStatement createMusicListPS(Range range, SearchResultSort sort) throws SQLException {
		String sorting = (sort == null) ? "ASC" : (sort.isAscending() ? "ASC" : "DESC");
		String limit = (range == null) ? "" : " LIMIT " + range.getStart() + " , " + range.getLength();

		String sql = "SELECT * FROM `recording` WHERE `rec_title` LIKE ?"
				+ " ORDER BY `recording`.`rec_title` " + sorting + limit;
		return getStatement(sql);
	}

	private PreparedStatement createReleaseListPS(Range range, SearchResultSort sort) throws SQLException {
		String sorting = (sort == null) ? "ASC" : (sort.isAscending() ? "ASC" : "DESC");
		String limit = (range == null) ? "" : " LIMIT " + range.getStart() + " , " + range.getLength();

		String sql = "SELECT * FROM `production` WHERE `prod_res_title` LIKE ?"
				+ " ORDER BY `production`.`prod_res_title` " + sorting + limit;
		return getStatement(sql);
	}

	private PreparedStatement createSoundtrackListPS(Range range, SearchResultSort sort) throws SQLException {
		String sorting = (sort == null) ? "ASC" : (sort.isAscending() ? "ASC" : "DESC");
		String limit = (range == null) ? "" : " LIMIT " + range.getStart() + " , " + range.getLength();

		String sql = "SELECT * FROM `soundtrack` WHERE `stk_interna` LIKE ?"
				+ " ORDER BY `soundtrack`.`stk_interna` " + sorting + limit;
		return getStatement(sql);
	}
}

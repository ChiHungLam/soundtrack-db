package dev.sdb.server.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.SearchResultSort;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

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

	public void initReleases(Range range, SearchResultSort sort) throws IOException {
		try {
			this.releaseCountPS = createReleaseCountPS();
			this.releaseListPS = createReleaseListPS(range, sort);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	public void initMusic(Range range, SearchResultSort sort) throws IOException {
		try {
			this.musicCountPS = createMusicCountPS();
			this.musicListPS = createMusicListPS(range, sort);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	public void initSoundtracks(Range range, SearchResultSort sort) throws IOException {
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
				Music music = readMusic(rs);
				result.add(music);
			}

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}

	}

	protected Music readMusic(ResultSet rs) throws SQLException {
		long id = rs.getLong("rec_id");
		String title = rs.getString("rec_title");
		return new Music(id, title);
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
				Release release = null;//readRelease(rs);
				Music music = null;//readMusic(rs);
				Soundtrack soundtrack = readSoundtrack(rs, release, music);

				result.add(soundtrack);
			}

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			closeResultSet(rs);
		}
	}

	protected Soundtrack readSoundtrack(ResultSet rs, Release release, Music music) throws SQLException {
		long id = rs.getLong("stk_id");
		return new Soundtrack(id, release, music);
	}

	public void queryReleases(List<Entity> result, String term) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			this.releaseListPS.clearParameters();
			this.releaseListPS.setString(1, wildcardedTerm);

			rs = this.releaseListPS.executeQuery();

			while (rs.next()) {
				Release release = readRelease(rs);
				result.add(release);
			}

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			closeResultSet(rs);
		}
	}

	protected Release readRelease(ResultSet rs) throws SQLException {
		long id = rs.getLong("prod_id");
		String title = rs.getString("prod_res_title");

		return new Release(id, title);
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

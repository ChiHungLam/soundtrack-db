package dev.sdb.server.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.gwt.view.client.Range;

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

	public void initReleases(Range range, boolean ascending) throws IOException {
		try {
			this.releaseCountPS = createReleaseCountPS();
			this.releaseListPS = createReleaseListPS(range, ascending);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	public void initMusic(Range range, boolean ascending) throws IOException {
		try {
			this.musicCountPS = createMusicCountPS();
			this.musicListPS = createMusicListPS(range, ascending);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
		}
	}

	public void initSoundtracks(Range range, boolean ascending) throws IOException {
		try {
			this.soundtrackCountPS = createSoundtrackCountPS();
			this.soundtrackListPS = createSoundtrackListPS(range, ascending);
		} catch (SQLException e) {
			close();
			throw new IOException(e);
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

	public void querySoundtracks(List<Entity> result, String term) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			this.soundtrackListPS.clearParameters();
			this.soundtrackListPS.setString(1, wildcardedTerm);

			rs = this.soundtrackListPS.executeQuery();

			while (rs.next()) {
				Soundtrack soundtrack = readSoundtrack(rs);

				result.add(soundtrack);
			}

		} catch (SQLException e) {
			throw new IOException(e);

		} finally {
			closeResultSet(rs);
		}
	}

	protected Release readRelease(ResultSet rs) throws SQLException {
		long id = rs.getLong("print_id");
		String title = rs.getString("prod_res_title");

		return new Release(id, title);
	}

	protected Music readMusic(ResultSet rs) throws SQLException {
		long id = rs.getLong("vers_id");
		String title = rs.getString("rec_title");
		return new Music(id, title);
	}

	protected Soundtrack readSoundtrack(ResultSet rs) throws SQLException {
		Release release = readRelease(rs);
		Music music = readMusic(rs);

		long id = rs.getLong("stk_id");
		return new Soundtrack(id, release, music);
	}

	private String getOrderDirection(boolean ascending) {
		return ascending ? "ASC" : "DESC";
	}

	private String getLimit(Range range) {
		return (range == null) ? ";" : "LIMIT " + range.getStart() + " , " + range.getLength() + " ;";
	}

	private PreparedStatement createReleaseCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `prod_res_title` LIKE ? ;";
		return getStatement(sql);
	}

	private PreparedStatement createMusicCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `version` "
				+ "LEFT JOIN `part` ON `part_id` = `vers_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `part_recording_id` "
				+ "WHERE `rec_title` LIKE ? ;";
		return getStatement(sql);
	}

	private PreparedStatement createSoundtrackCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "WHERE `rec_title` LIKE ? ;";
		return getStatement(sql);
	}

	private PreparedStatement createReleaseListPS(Range range, boolean ascending) throws SQLException {
		String sql = "SELECT `print`.* , `prod_res_title` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `prod_res_title` LIKE ? "
				+ "ORDER BY `prod_res_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return getStatement(sql);
	}

	private PreparedStatement createMusicListPS(Range range, boolean ascending) throws SQLException {
		String sql = "SELECT `version`.* , `rec_title` "
				+ "FROM `version` "
				+ "LEFT JOIN `part` ON `part_id` = `vers_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `part_recording_id` "
				+ "WHERE `rec_title` LIKE ? "
				+ "ORDER BY `rec_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return getStatement(sql);
	}

	private PreparedStatement createSoundtrackListPS(Range range, boolean ascending) throws SQLException {
		String sql = "SELECT `soundtrack`.* , `vers_id` , `rec_title` , `print_id` , `prod_res_title` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `seq_set` ON `sqst_id` = `seq_seqset_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `sqst_production_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `prod_main_release_id` "
				+ "LEFT JOIN `print` ON `print_id` = `rel_main_print_id` "
				+ "WHERE `rec_title` LIKE ? "
				+ "ORDER BY `rec_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return getStatement(sql);
	}
}

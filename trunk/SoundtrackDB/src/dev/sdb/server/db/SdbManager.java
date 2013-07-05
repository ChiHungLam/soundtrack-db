package dev.sdb.server.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.SearchResult;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public class SdbManager extends SqlManager {

	//	private PreparedStatement countPS;
	//	private PreparedStatement listPS;

	public SdbManager(SqlServer sqlServer) {
		super(sqlServer);
	}

	@Override public void open() throws IOException {
		openConnection();
	}

	@Override public void close() {
		//		closeStatement(this.countPS);
		//		closeStatement(this.listPS);

		closeConnection();

		//		this.countPS = null;
		//		this.listPS = null;
	}

	//	public void initReleases(Range range, boolean ascending) throws IOException {
	//		try {
	//			this.countPS = createReleaseCountPS();
	//			this.listPS = createReleaseListPS(range, ascending);
	//		} catch (SQLException e) {
	//			close();
	//			throw new IOException(e);
	//		}
	//	}

	//	public void initMusic(Range range, boolean ascending) throws IOException {
	//		try {
	//			
	//		} catch (SQLException e) {
	//			close();
	//			throw new IOException(e);
	//		}
	//	}

	//	public void initSoundtracks(Range range, boolean ascending) throws IOException {
	//		try {
	//			this.countPS = createSoundtrackCountPS();
	//			this.listPS = createSoundtrackListPS(range, ascending);
	//		} catch (SQLException e) {
	//			close();
	//			throw new IOException(e);
	//		}
	//	}


	public SearchResult query(Flavor flavor, String term, Range range, boolean ascending) throws IOException {
		PreparedStatement listPS = null;
		PreparedStatement countPS = null;

		List<Entity> result = new Vector<Entity>();

		try {

			int count;

			switch (flavor) {
			case RELEASES:
				listPS = createReleaseListPS(range, ascending);
				countPS = createReleaseCountPS();
				count = count(countPS, term);
				queryReleases(listPS, result, term, range, ascending);

				break;
			case MUSIC:
				listPS = createMusicListPS(range, ascending);
				countPS = createMusicCountPS();
				count = count(countPS, term);
				queryMusic(listPS, result, term, range, ascending);

				break;
			case SOUNDTRACK:
				listPS = createSoundtrackListPS(range, ascending);
				countPS = createSoundtrackCountPS();
				count = count(countPS, term);
				querySoundtracks(listPS, result, term, range, ascending);

				break;

			default:
				result.clear();
				throw new IllegalArgumentException("illegal flavor: " + flavor);
			}

			String info = "Suchergebnis für: '" + term + "'.";
			if (count > 0)
				info += " Gefundene Einträge: " + count;

			return new SearchResult(info, result, count);

		} catch (SQLException e) {
			result.clear();
			throw new IOException(e);
		} finally {
			closeStatement(listPS);
			closeStatement(countPS);
		}

	}

	public Entity getEntity(Flavor flavor, long id) throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			switch (flavor) {
			case RELEASES:
				ps = getStatement(composeReleaseGet());
				ps.setLong(1, id);

				rs = ps.executeQuery();
				if (!rs.next())
					return null;
				return readRelease(rs);

			case MUSIC:
				ps = getStatement(composeMusicGet());
				ps.setLong(1, id);

				rs = ps.executeQuery();
				if (!rs.next())
					return null;
				return readMusic(rs);

			case SOUNDTRACK:
				ps = getStatement(composeSoundtrackGet());
				ps.setLong(1, id);

				rs = ps.executeQuery();
				if (!rs.next())
					return null;
				return readSoundtrack(rs);

			default:
				throw new IllegalArgumentException("unknown flavor: " + flavor.name());
			}
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
		}
	}

	protected int count(PreparedStatement ps, String term) throws IOException {

		ResultSet rs = null;

		try {

			ps.clearParameters();
			ps.setString(1, "%" + term + "%");

			rs = ps.executeQuery();
			if (!rs.next())
				return 0;

			return rs.getInt("rows");

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}
	}

	protected void queryReleases(final PreparedStatement ps, List<Entity> result, String term, Range range, boolean ascending) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			ps.clearParameters();
			ps.setString(1, wildcardedTerm);

			rs = ps.executeQuery();

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

	protected void queryMusic(final PreparedStatement ps, List<Entity> result, String term, Range range, boolean ascending) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			ps.clearParameters();
			ps.setString(1, wildcardedTerm);

			rs = ps.executeQuery();

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

	protected void querySoundtracks(final PreparedStatement ps, List<Entity> result, String term, Range range, boolean ascending) throws IOException {
		ResultSet rs = null;

		try {

			String wildcardedTerm = "%" + term + "%";

			ps.clearParameters();
			ps.setString(1, wildcardedTerm);

			rs = ps.executeQuery();

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

	private String composeReleaseGet() throws SQLException {
		String sql = "SELECT `print`.* , `prod_res_title` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `print_id` = ? ;";
		return sql;
	}

	private String composeMusicGet() throws SQLException {
		String sql = "SELECT `version`.* , `rec_title` "
				+ "FROM `version` "
				+ "LEFT JOIN `part` ON `part_id` = `vers_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `part_recording_id` "
				+ "WHERE `vers_id` = ? ;";
		return sql;
	}

	private String composeSoundtrackGet() throws SQLException {
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
				+ "WHERE `stk_id` = ? ;";
		return sql;
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

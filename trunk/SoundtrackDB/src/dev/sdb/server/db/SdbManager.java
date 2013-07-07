package dev.sdb.server.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Soundtrack;

public class SdbManager extends SqlManager {

	private static final String PRINT_INFO_FIELDS = "" +
			"`print_id` , " +
			"`print_audio_id` , " +
			"`print_or_res_title` , " +
			"`print_status` , " +
			"`print_catalog_number` , " +
			"`print_order` , " +
			"`prod_res_title` , " +
			"`lab_titel` , " +
			"`mm_name` , " +
			"`perf_name` , " +
			"`cal_date_1st` , " +
			"`au_total_length`";


			

	public SdbManager(SqlServer sqlServer) {
		super(sqlServer);
	}

	@Override public void open() throws IOException {
		openConnection();
	}

	@Override public void close() {
		closeConnection();
	}

	public Result getMusicReleaseList(long versionId, Range range) throws IOException {
		PreparedStatement listPS = null;
		PreparedStatement countPS = null;

		List<Entity> result = new Vector<Entity>();

		try {

			listPS = createMusicReleaseListPS(range);
			countPS = createMusicReleaseListCountPS();
			int count = count(countPS, Long.valueOf(versionId));
			queryMusicReleaseList(listPS, result, versionId, range);

			return new Result(result, count);

		} catch (SQLException e) {
			result.clear();
			throw new IOException(e);
		} finally {
			closeStatement(listPS);
			closeStatement(countPS);
		}
	}




	public Result getReleaseSoundtrackList(long audioId, Range range) throws IOException {
		PreparedStatement listPS = null;
		PreparedStatement countPS = null;

		List<Entity> result = new Vector<Entity>();

		try {

			listPS = createReleaseSoundtrackListPS(range);
			countPS = createReleaseSoundtrackListCountPS();
			int count = count(countPS, Long.valueOf(audioId));
			queryReleaseSoundtrackList(listPS, result, audioId, range);

			return new Result(result, count);

		} catch (SQLException e) {
			result.clear();
			throw new IOException(e);
		} finally {
			closeStatement(listPS);
			closeStatement(countPS);
		}
	}

	public Result query(Flavor flavor, String term, Range range, boolean ascending) throws IOException {
		PreparedStatement listPS = null;
		PreparedStatement countPS = null;

		String wildcardedTerm = "%" + term + "%";

		List<Entity> result = new Vector<Entity>();

		try {

			int count;

			switch (flavor) {
			case RELEASES:
				listPS = createReleaseListPS(range, ascending);
				countPS = createReleaseCountPS();
				count = count(countPS, wildcardedTerm);
				queryReleases(listPS, result, wildcardedTerm, range, ascending);

				break;
			case MUSIC:
				listPS = createMusicListPS(range, ascending);
				countPS = createMusicCountPS();
				count = count(countPS, wildcardedTerm);
				queryMusic(listPS, result, wildcardedTerm, range, ascending);

				break;
			case SOUNDTRACK:
				listPS = createSoundtrackListPS(range, ascending);
				countPS = createSoundtrackCountPS();
				count = count(countPS, wildcardedTerm);
				querySoundtracks(listPS, result, wildcardedTerm, range, ascending);

				break;

			default:
				result.clear();
				throw new IllegalArgumentException("illegal flavor: " + flavor);
			}

			return new Result(result, count);

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

	protected int count(PreparedStatement ps, Object whereArg) throws IOException {

		ResultSet rs = null;

		try {

			ps.clearParameters();
			ps.setObject(1, whereArg);

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

			ps.clearParameters();
			ps.setString(1, term);

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

			ps.clearParameters();
			ps.setString(1, term);

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

			ps.clearParameters();
			ps.setString(1, term);

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

	protected void queryMusicReleaseList(PreparedStatement ps, List<Entity> result, long versionId, Range range) throws IOException {
		ResultSet rs = null;

		try {

			ps.clearParameters();
			ps.setLong(1, versionId);

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

	protected void queryReleaseSoundtrackList(PreparedStatement ps, List<Entity> result, long audioId, Range range) throws IOException {
		ResultSet rs = null;

		try {

			ps.clearParameters();
			ps.setLong(1, audioId);

			rs = ps.executeQuery();

			while (rs.next()) {
				Soundtrack soundtrack = readSoundtrack(rs, false, true);
				result.add(soundtrack);
			}

		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			closeResultSet(rs);
		}
	}

	// 
	protected Release readRelease(ResultSet rs) throws SQLException {
		long id = rs.getLong("print_id");
		long audioId = rs.getLong("print_audio_id");

		String title = rs.getString("print_or_res_title");
		if (title == null || title.isEmpty())
			title = rs.getString("prod_res_title");

		int printStatus = rs.getInt("print_status");
		String catalogNumber = rs.getString("print_catalog_number");
		int print = rs.getInt("print_order");

		String label = rs.getString("lab_titel");
		String media = rs.getString("mm_name");
		String artist = rs.getString("perf_name");
		Date date = rs.getDate("cal_date_1st");
		Date duration = rs.getDate("au_total_length");

		@SuppressWarnings("deprecation") int year = (date == null) ? 0 : date.getYear() + 1900;

		return new Release(id, audioId, artist, title, label, media, catalogNumber, print, year, printStatus, duration);
	}

	protected Music readMusic(ResultSet rs) throws SQLException {
		long id = rs.getLong("vers_id");
		String title = rs.getString("rec_title");
		return new Music(id, title);
	}

	protected Soundtrack readSoundtrack(ResultSet rs) throws SQLException {
		return readSoundtrack(rs, true, true);
	}

	protected Soundtrack readSoundtrack(ResultSet rs, boolean readRelease, boolean readMusic) throws SQLException {
		Release release = readRelease ? readRelease(rs) : null;
		Music music = readMusic ? readMusic(rs) : null;

		long id = rs.getLong("stk_id");
		return new Soundtrack(id, release, music);
	}

	private String getOrderDirection(boolean ascending) {
		return ascending ? "ASC" : "DESC";
	}

	private String getLimit(Range range) {
		//return (range == null) ? ";" : "LIMIT " + range.getStart() + " , " + range.getLength() + " ;";
		return "LIMIT " + range.getStart() + " , " + range.getLength() + " ;";
	}

	private String composePrintOrder(boolean ascending) {
		String direction = getOrderDirection(ascending);
		//`cal_order`
		return "`cat_hierar_sort` " + direction + " , `rel_catalog_index` " + direction + " , `print_order` " + direction;
	}

	private String composeReleaseGet() throws SQLException {
		String sql = "SELECT " + PRINT_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
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
		String sql = "SELECT `soundtrack`.* , `vers_id` , `rec_title` , " + PRINT_INFO_FIELDS + " "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `seq_set` ON `sqst_id` = `seq_seqset_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `sqst_production_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `prod_main_release_id` "
				+ "LEFT JOIN `print` ON `print_id` = `rel_main_print_id` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
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

	private PreparedStatement createReleaseSoundtrackListCountPS() throws SQLException {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `aus_audio_id` LIKE ? ;";
		return getStatement(sql);
	}

	private PreparedStatement createMusicReleaseListCountPS() throws SQLException {
		String inSelect = "SELECT `aus_audio_id` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `stk_version_id` LIKE ?";

		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `print_audio_id` IN ( " + inSelect + " ) ;";
		return getStatement(sql);
	}

	private PreparedStatement createReleaseListPS(Range range, boolean ascending) throws SQLException {
		String sql = "SELECT " + PRINT_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `prod_res_title` LIKE ? "
				+ "ORDER BY " + composePrintOrder(ascending) + " "
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
		String sql = "SELECT `soundtrack`.* , `vers_id` , `rec_title` , " + PRINT_INFO_FIELDS + " "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `seq_set` ON `sqst_id` = `seq_seqset_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `sqst_production_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `prod_main_release_id` "
				+ "LEFT JOIN `print` ON `print_id` = `rel_main_print_id` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "WHERE `rec_title` LIKE ? "
				+ "ORDER BY `rec_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return getStatement(sql);
	}

	private PreparedStatement createReleaseSoundtrackListPS(Range range) throws SQLException {
		String sql = "SELECT `soundtrack`.* , `vers_id` , `rec_title` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `aus_audio_id` LIKE ? "
				+ "ORDER BY `aus_order` ASC , `seq_order` ASC , `stk_order` ASC "
				+ getLimit(range);
		return getStatement(sql);
	}

	private PreparedStatement createMusicReleaseListPS(Range range) throws SQLException {
		String inSelect = "SELECT `aus_audio_id` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `stk_version_id` LIKE ?";

		String sql = "SELECT " + PRINT_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `print_audio_id` IN ( " + inSelect + " ) "
				+ "ORDER BY " + composePrintOrder(true) + " "
				+ getLimit(range);
		return getStatement(sql);
	}

}
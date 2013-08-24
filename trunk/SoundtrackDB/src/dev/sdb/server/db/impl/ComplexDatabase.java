package dev.sdb.server.db.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Vector;

import com.google.gwt.view.client.Range;

import dev.sdb.server.db.SqlServer;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Genre;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public class ComplexDatabase extends AbstractDatabase implements ComplexSchema {

	private static final String ARTWORTK_ROOT = "http://localhost/mimg/";
	private static final String NO_ARTWORK_DIRECTORY = "unknown/";
	private static final String NO_ARTWORK_IMAGE = "missing.png";

	private static final String SERIES_INFO_FIELDS = "" +
			"`edt_id` , " +
			"`edt_status` , " +
			"`edt_override_title` , " +
			"`edt_abbrev` , " +
			"`edt_singles` , " +
			"`ser_various`";

	private static final String RELEASE_INFO_FIELDS = "" +
			"`print_id` , " +
			"`print_audio_id` , " +
			"`print_or_res_title` , " +
			"`print_status` , " +
			"`print_catalog_number` , " +
			"`print_order` , " +
			"`rel_status` , " +
			"`prod_res_title` , " +
			"`prod_status` , " +
			"`prod_episode` , " +
			"`lab_titel` , " +
			"`typ_name` , " +
			"`typ_status` , " +
			SERIES_INFO_FIELDS + " , " +
			"`mm_name` , " +
			//			"`perf_name` , " +
			"`cal_date_1st` , " +
			"`au_total_length` , " +
			"`cat_artwork_root`";

	private static final String GENRE_INFO_FIELDS = "" +
			"`gen_id` , " +
			"`gen_status` , " +
			"`gen_hierar_parent_name` , " +
			"`gen_hierar_child_name`";

	private static final String SOUNDTRACK_INFO_FIELDS = "" +
			"`stk_id` , " +
			"`stk_order` , " +
			"`stk_start_time` , " +
			"`stk_stop_time` , " +
			"`stk_status` , " +
			"`seq_amount` , " +
			"`seq_order` , " +
			"`seq_status` , " +
			"`aus_side_index` , " +
			"`aus_offset_seq` , " +
			"`aus_offset_time` , " +
			"`aus_shortage`";

	private static final String MUSIC_INFO_FIELDS = "" +
			"`vers_id` , " +
			"`vers_order` , " +
			"`vers_name_full` , " +
			"`vers_status` , " +
			"`vers_year` , " +
			"`part_order` , " +
			"`part_status` , " +
			"`part_title` , " +
			"`rec_status` , " +
			GENRE_INFO_FIELDS + " , " +
			"`auts_display` , " +
			"`perf_name`";

	private static final String CATALOG_INFO_FIELDS = "" +
			"`cat_id` , " +
			"`cat_title` , " +
			"`cat_parent_id` , " +
			"`cat_children` , " +
			"`cat_info_catalog` , " +
			"`cat_start` , " +
			"`cat_end`";

	public ComplexDatabase(SqlServer sqlServer) {
		super(sqlServer);
	}

	@Override public void repairCatalogChildInfo() throws IOException {
		PreparedStatement selectPS = null;
		PreparedStatement countPS = null;
		PreparedStatement updatePS = null;

		List<Entity> result = new Vector<Entity>();

		try {

			String sqlSelect = "SELECT `cat_id` , `cat_children` FROM `catalog` ;";
			String sqlCount = "SELECT COUNT( * ) AS `rows` FROM `catalog` WHERE `catalog`.`cat_parent_id` = ? ;";
			String sqlUpdate = "UPDATE `sdb`.`catalog` SET `cat_children` = ? WHERE `catalog`.`cat_id` = ? LIMIT 1 ;";

			selectPS = getStatement(sqlSelect);
			countPS = getStatement(sqlCount);
			updatePS = getUpdatableStatement(sqlUpdate);

			//			queryCatalogLevelList(listPS, result, parentId);

			ResultSet rsSelect = null;

			try {

				rsSelect = selectPS.executeQuery();

				while (rsSelect.next()) {
					long id = rsSelect.getLong("cat_id");
					boolean children = rsSelect.getBoolean("cat_children");

					int count = count(countPS, Long.valueOf(id));
					boolean currentChildren = (count > 0);

					if (children != currentChildren) {

						try {

							updatePS.clearParameters();
							updatePS.setBoolean(1, currentChildren);
							updatePS.setLong(2, id);

							updatePS.executeUpdate();

						} catch (SQLException e) {
							throw new IOException(e);
						}
					}
				}

			} catch (SQLException e) {
				throw new IOException(e);
			} finally {
				closeResultSet(rsSelect);
			}

		} catch (SQLException e) {
			result.clear();
			throw new IOException(e);
		} finally {
			closeStatement(updatePS);
			closeStatement(countPS);
			closeStatement(selectPS);
		}
	}

	protected Release readRelease(ResultSet rs) throws SQLException {
		long id = rs.getLong("print_id");
		if (id <= 0)
			return null;

		long audioId = rs.getLong("print_audio_id");

		String title = rs.getString("print_or_res_title");
		if (title == null || title.isEmpty())
			title = rs.getString("prod_res_title");

		int typeStatus = rs.getInt("typ_status");
		int productionStatus = rs.getInt("prod_status");
		int releaseStatus = rs.getInt("rel_status");
		int printStatus = rs.getInt("print_status");

		String catalogNumber = rs.getString("print_catalog_number");
		int print = rs.getInt("print_order");

		String label = rs.getString("lab_titel");
		String media = rs.getString("mm_name");
		//		String artist = rs.getString("perf_name");
		java.sql.Date date = rs.getDate("cal_date_1st");
		Time duration = rs.getTime("au_total_length");

		String type = rs.getString("typ_name");

		int episode = rs.getInt("prod_episode");

		String artworkRootUrl = rs.getString("cat_artwork_root");

		Series series = readSeries(rs);

		int year = getYearFromDate(date);
		int durationSeconds = getSecondsFromTime(duration);
		String artworkUrl = getArtworkUrl(artworkRootUrl, label, catalogNumber, print);

		return new Release(id, type, series, artworkUrl, episode, title, label, media, catalogNumber, print, year, typeStatus, productionStatus, releaseStatus, printStatus, durationSeconds, audioId);
	}

	private String getArtworkUrl(String artworkRootUrl, String label, String catalogNumber, int print) {
		try {
			if (artworkRootUrl == null || artworkRootUrl.isEmpty())
				throw new IllegalStateException();
			if (catalogNumber == null || catalogNumber.isEmpty())
				throw new IllegalStateException();

			catalogNumber = catalogNumber.replace("/", "-");

			String url = ARTWORTK_ROOT + artworkRootUrl + catalogNumber;
			if (print > 1)
				url += ".P" + print;
			return url + ".jpg";
			
		} catch (IllegalStateException e) {
			if (label == null || label.isEmpty())
				return ARTWORTK_ROOT + NO_ARTWORK_DIRECTORY + NO_ARTWORK_IMAGE;

			return ARTWORTK_ROOT + NO_ARTWORK_DIRECTORY + label.toLowerCase() + ".png";
		}


	}

	protected Series readSeries(ResultSet rs) throws SQLException {
		long id = rs.getLong("edt_id");
		if (id <= 0)
			return null;

		int editionStatus = rs.getInt("edt_status");
		String title = rs.getString("edt_override_title");
		String shortTitle = rs.getString("edt_abbrev");

		boolean singles = rs.getBoolean("ser_various");
		if (!singles)
			singles = rs.getBoolean("edt_singles");

		return new Series(id, title, shortTitle, editionStatus, singles);
	}

	@Override protected Catalog readCatalog(ResultSet rs) throws SQLException {
		long id = rs.getLong("cat_id");
		if (id <= 0)
			return null;

		String title = rs.getString("cat_title");
		String info = rs.getString("cat_info_catalog");
		long parentId = rs.getLong("cat_parent_id");
		boolean children = rs.getBoolean("cat_children");
		int eraBegin = rs.getInt("cat_start");
		int eraEnd = rs.getInt("cat_end");

		return new Catalog(id, parentId, children, title, info, eraBegin, eraEnd);
	}
	protected Music readMusic(ResultSet rs) throws SQLException {
		long id = rs.getLong("vers_id");
		if (id <= 0)
			return null;

		int versionOrder = rs.getInt("vers_order");
		String versionName = rs.getString("vers_name_full");
		int year = rs.getInt("vers_year");

		int partOrder = rs.getInt("part_order");
		String title = rs.getString("part_title");

		int versionStatus = rs.getInt("vers_status");
		int partStatus = rs.getInt("part_status");
		int recStatus = rs.getInt("rec_status");

		String authors = rs.getString("auts_display");
		String artist = rs.getString("perf_name");

		Time duration = null;

		Genre genre = readGenre(rs);

		int durationSeconds = getSecondsFromTime(duration);

		return new Music(id, genre, title, versionName, year, authors, artist, partOrder, versionOrder, recStatus, partStatus, versionStatus, durationSeconds);
	}

	protected Genre readGenre(ResultSet rs) throws SQLException {
		long id = rs.getLong("gen_id");
		if (id <= 0)
			return null;

		int status = rs.getInt("gen_status");
		String parentName = rs.getString("gen_hierar_parent_name");
		String childName = rs.getString("gen_hierar_child_name");

		return new Genre(id, status, parentName, childName);
	}

	protected Soundtrack readSoundtrack(ResultSet rs, boolean readRelease, boolean readMusic) throws SQLException {
		long id = rs.getLong("stk_id");

		if (id <= 0)
			return null;

		int stkOrder = rs.getInt("stk_order");
		// int stkStatus = rs.getInt("stk_status");
		Time startTime = rs.getTime("stk_start_time");
		Time stopTime = rs.getTime("stk_stop_time");

		int seqOrder = rs.getInt("seq_order");
		// int seqStatus = rs.getInt("seq_status");
		int seqAmount = rs.getInt("seq_amount");

		int ausSideIndex = rs.getInt("aus_side_index");
		int ausSeqOffset = rs.getInt("aus_offset_seq");
		Time ausTimeOffset = rs.getTime("aus_offset_time");
		long ausTimeShortageSec = rs.getLong("aus_shortage");

		int startSec = getSecondsFromTime(startTime);
		int stopSec = getSecondsFromTime(stopTime);
		int ausTimeOffsetSec = getSecondsFromTime(ausTimeOffset);
		
		startSec += ausTimeOffsetSec;
		stopSec += ausTimeOffsetSec;

		if (seqOrder == 1 && ausTimeShortageSec > 0) {
			//startTime -= ausTimeShortageSec;
		}

		seqOrder += ausSeqOffset;

		String seqNum;

		switch (ausSideIndex) {
		case -1:
			seqNum = "";
			break;
		case 0:
			seqNum = "?";
			break;
		default:
			seqNum = Character.toString((char) (64 + ausSideIndex));
			break;
		}

		if (seqOrder > 0)
			seqNum += seqOrder;
		if (seqAmount > 1 && stkOrder > 0)
			seqNum += (seqNum.isEmpty() ? "" : "-") + getRomanNumeral(stkOrder);

		Release release = readRelease ? readRelease(rs) : null;
		Music music = readMusic ? readMusic(rs) : null;

		return new Soundtrack(id, release, music, ausSideIndex, seqNum, startSec, stopSec);
	}

	private int getSecondsFromTime(Time time) {
		if (time == null)
			return -1;
			
		String timeString = time.toString();
		if (timeString.length() != 8)
			return -1;

		//01:34:67
		
		int seconds = Integer.parseInt(timeString.substring(6));
		int minutes = Integer.parseInt(timeString.substring(3, 5));
		int hours = Integer.parseInt(timeString.substring(0, 2));
		
		return seconds + (60 * minutes) + (3600 * hours);
	}

	@SuppressWarnings("deprecation") private int getYearFromDate(java.sql.Date date) {
		return (date == null) ? 0 : date.getYear() + 1900;
	}

	private String getRomanNumeral(int stkOrder) {
		if (stkOrder <= 0)
			return "";

		switch (stkOrder) {
		case 1:
			return "I";
		case 2:
			return "II";
		case 3:
			return "III";
		case 4:
			return "IV";
		case 5:
			return "V";
		case 6:
			return "VI";
		case 7:
			return "VII";
		case 8:
			return "VIII";
		case 9:
			return "IX";
		case 10:
			return "X";
		case 11:
			return "XI";
		case 12:
			return "XII";
		case 13:
			return "XIII";
		case 14:
			return "XIV";
		case 15:
			return "XV";
		case 16:
			return "XVI";
		case 17:
			return "XVII";
		case 18:
			return "XVIII";
		case 19:
			return "XIX";
		case 20:
			return "XX";

		default:
			return "" + stkOrder;
		}

	}

	private String composePrintOrder(boolean ascending) {
		String direction = getOrderDirection(ascending);//`cal_order`
		return "`cat_hierar_sort` " + direction + " , `rel_catalog_index` " + direction + " , `print_order` " + direction;
	}

	protected String composeReleaseGet() {
		String sql = "SELECT " + RELEASE_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "WHERE `print_id` = ? ;";
		return sql;
	}

	protected String composeMusicGet() {
		String sql = "SELECT " + MUSIC_INFO_FIELDS + " "
				+ "FROM `version` "
				+ "LEFT JOIN `part` ON `part_id` = `vers_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `part_recording_id` "
				+ "LEFT JOIN `genre` ON `gen_id` = `rec_genre_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `vers_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `author_set` ON `auts_id` = `vers_authorset_id` "
				+ "WHERE `vers_id` = ? ;";
		return sql;
	}

	protected String composeCatalogGet() {
		String sql = "SELECT " + CATALOG_INFO_FIELDS + " "
				+ "FROM `catalog` "
				+ "WHERE `cat_id` = ? ;";
		return sql;
	}

	@Override protected String composeCatalogLevelList() {
		String sql = "SELECT " + CATALOG_INFO_FIELDS + " "
				+ "FROM `catalog` "
				+ "WHERE `cat_parent_id` = ? "
				+ "ORDER BY `catalog`.`cat_hierar_sort` ASC ;";
		return sql;
	}

	protected String composeSeriesGet() {
		String sql = "SELECT " + SERIES_INFO_FIELDS + " "
				+ "FROM `edition` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `edt_id` = ? ;";
		return sql;
	}

	protected String composeSoundtrackGet() {
		String sql = "SELECT " + SOUNDTRACK_INFO_FIELDS + " , " + MUSIC_INFO_FIELDS + " , " + RELEASE_INFO_FIELDS + " "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `genre` ON `gen_id` = `rec_genre_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `vers_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `author_set` ON `auts_id` = `vers_authorset_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `seq_set` ON `sqst_id` = `seq_seqset_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `sqst_production_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `prod_main_release_id` "
				+ "LEFT JOIN `print` ON `print_id` = `rel_main_print_id` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `audio_set` ON ( `aus_seqset_id` = `seq_seqset_id` AND `aus_audio_id` = `print_audio_id` ) "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "WHERE `stk_id` = ? ;";
		return sql;
	}

	protected String composeReleaseCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `prod_res_title` LIKE ? ;";
		return sql;
	}

	protected String composeMusicCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `version` "
				+ "LEFT JOIN `part` ON `part_id` = `vers_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `part_recording_id` "
				+ "WHERE `rec_title` LIKE ? ;";
		return sql;
	}

	protected String composeSeriesCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `edition` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `edt_override_title` LIKE ? ;";
		return sql;
	}

	protected String composeSoundtrackCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "WHERE `rec_title` LIKE ? ;";
		return sql;
	}

	//	protected String composeReleaseSoundtrackListCount() {
	//		String sql = "SELECT COUNT( * ) AS `rows` "
	//				+ "FROM `soundtrack` "
	//				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
	//				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
	//				+ "WHERE `aus_audio_id` LIKE ? ;";
	//		return sql;
	//	}

	protected String composeMusicReleaseListCount() {
		String inSelect = "SELECT `aus_audio_id` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `stk_version_id` LIKE ?";

		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `print` "
				//				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				//				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `print_audio_id` IN ( " + inSelect + " ) ;";
		return sql;
	}

	protected String composeSeriesReleaseListCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "WHERE `prod_edition_id` LIKE ? ;";
		return sql;
	}


	@Override protected String composeCatalogReleaseList(Range range, boolean ascending) {
		String sql = "SELECT " + RELEASE_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `rel_catalog_id` LIKE ? "
				+ "ORDER BY " + composePrintOrder(ascending) + " "
				+ getLimit(range);
		return sql;
	}

	@Override protected String composeCatalogReleaseListCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `print` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "WHERE `rel_catalog_id` LIKE ? ;";
		return sql;
	}

	protected String composeReleaseList(Range range, boolean ascending) {
		String sql = "SELECT " + RELEASE_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `prod_res_title` LIKE ? "
				+ "ORDER BY " + composePrintOrder(ascending) + " "
				+ getLimit(range);
		return sql;
	}
	protected String composeMusicList(Range range, boolean ascending) {
		String sql = "SELECT " + MUSIC_INFO_FIELDS + " "
				+ "FROM `version` "
				+ "LEFT JOIN `part` ON `part_id` = `vers_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `part_recording_id` "
				+ "LEFT JOIN `genre` ON `gen_id` = `rec_genre_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `vers_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `author_set` ON `auts_id` = `vers_authorset_id` "
				+ "WHERE `rec_title` LIKE ? "
				+ "ORDER BY `rec_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return sql;
	}

	protected String composeSeriesList(Range range, boolean ascending) {
		String sql = "SELECT " + SERIES_INFO_FIELDS + " "
				+ "FROM `edition` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `edt_override_title` LIKE ? "
				+ "ORDER BY `edt_override_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return sql;
	}

	protected String composeSoundtrackList(Range range, boolean ascending) {
		String sql = "SELECT " + SOUNDTRACK_INFO_FIELDS + " , " + MUSIC_INFO_FIELDS + " , " + RELEASE_INFO_FIELDS + " "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `genre` ON `gen_id` = `rec_genre_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `vers_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `author_set` ON `auts_id` = `vers_authorset_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `seq_set` ON `sqst_id` = `seq_seqset_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `sqst_production_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `prod_main_release_id` "
				+ "LEFT JOIN `print` ON `print_id` = `rel_main_print_id` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `audio_set` ON ( `aus_seqset_id` = `seq_seqset_id` AND `aus_audio_id` = `print_audio_id` ) "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "WHERE `rec_title` LIKE ? "
				+ "ORDER BY " + composePrintOrder(ascending) + " , "
					+ "`aus_order` " + getOrderDirection(ascending) + " , "
					+ "`seq_order` " + getOrderDirection(ascending) + " , "
					+ "`stk_order` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return sql;
	}


	protected String composeReleaseSoundtrackList() {
		String sql = "SELECT " + SOUNDTRACK_INFO_FIELDS + " , " + MUSIC_INFO_FIELDS + " "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `version` ON `vers_id` = `stk_version_id` "
				+ "LEFT JOIN `part` ON `part_id` = `stk_part_id` "
				+ "LEFT JOIN `recording` ON `rec_id` = `stk_recording_id` "
				+ "LEFT JOIN `genre` ON `gen_id` = `rec_genre_id` "
				+ "LEFT JOIN `lineup` ON `lin_id` = `vers_lineup_id` "
				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `author_set` ON `auts_id` = `vers_authorset_id` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `aus_audio_id` LIKE ? "
				+ "ORDER BY `aus_order` ASC , `seq_order` ASC , `stk_order` ASC ;";

		return sql;
	}

	protected String composeMusicReleaseList(Range range) {
		String inSelect = "SELECT `aus_audio_id` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `stk_version_id` LIKE ?";

		String sql = "SELECT " + RELEASE_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `print_audio_id` IN ( " + inSelect + " ) "
				+ "ORDER BY " + composePrintOrder(true) + " "
				+ getLimit(range);
		return sql;
	}
	
	protected String composeSeriesReleaseList(Range range) {
		String sql = "SELECT " + RELEASE_INFO_FIELDS + " "
				+ "FROM `print` "
				+ "LEFT JOIN `calendar` ON `cal_id` = `print_calendar_id` "
				+ "LEFT JOIN `audio` ON `au_id` = `print_audio_id` "
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `release` ON `rel_id` = `print_release_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "WHERE `prod_edition_id` LIKE ? "
				+ "ORDER BY " + composePrintOrder(true) + " "
				+ getLimit(range);
		return sql;
	}
}

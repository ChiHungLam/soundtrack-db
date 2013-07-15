package dev.sdb.server.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.google.gwt.view.client.Range;

import dev.sdb.server.db.SqlServer;
import dev.sdb.shared.model.entity.Genre;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public class ComplexDatabase extends AbstractDatabase implements ComplexSchema {

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
			"`au_total_length`";

	private static final String GENRE_INFO_FIELDS = "" +
			"`gen_id` , " +
			"`gen_status` , " +
			"`gen_hierar_parent_name` , " +
			"`gen_hierar_child_name`";

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

	public ComplexDatabase(SqlServer sqlServer) {
		super(sqlServer);
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
		Date date = rs.getDate("cal_date_1st");
		Date duration = rs.getDate("au_total_length");

		String type = rs.getString("typ_name");

		int episode = rs.getInt("prod_episode");

		@SuppressWarnings("deprecation") int year = (date == null) ? 0 : date.getYear() + 1900;

		Series series = readSeries(rs);

		return new Release(id, type, series, episode, title, label, media, catalogNumber, print, year, typeStatus, productionStatus, releaseStatus, printStatus, duration, audioId);
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

		Genre genre = readGenre(rs);

		return new Music(id, genre, title, versionName, year, authors, artist, partOrder, versionOrder, recStatus, partStatus, versionStatus);
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

		Release release = readRelease ? readRelease(rs) : null;
		Music music = readMusic ? readMusic(rs) : null;

		return new Soundtrack(id, release, music);
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

	protected String composeSeriesGet() {
		String sql = "SELECT " + SERIES_INFO_FIELDS + " "
				+ "FROM `edition` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `edt_id` = ? ;";
		return sql;
	}

	protected String composeSoundtrackGet() {
		String sql = "SELECT `soundtrack`.* , " + MUSIC_INFO_FIELDS + " , " + RELEASE_INFO_FIELDS + " "
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
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
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

	protected String composeReleaseSoundtrackListCount() {
		String sql = "SELECT COUNT( * ) AS `rows` "
				+ "FROM `soundtrack` "
				+ "LEFT JOIN `sequence` ON `seq_id` = `stk_sequence_id` "
				+ "LEFT JOIN `audio_set` ON `aus_seqset_id` = `seq_seqset_id` "
				+ "WHERE `aus_audio_id` LIKE ? ;";
		return sql;
	}

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
		String sql = "SELECT `soundtrack`.* , " + MUSIC_INFO_FIELDS + " , " + RELEASE_INFO_FIELDS + " "
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
				+ "LEFT JOIN `label` ON `lab_id` = `print_label_id` "
				//				+ "LEFT JOIN `lineup` ON `lin_id` = `print_lineup_id` "
				//				+ "LEFT JOIN `performer` ON `perf_id` = `lin_performer_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `rec_title` LIKE ? "
				+ "ORDER BY `rec_title` " + getOrderDirection(ascending) + " "
				+ getLimit(range);
		return sql;
	}


	protected String composeReleaseSoundtrackList(Range range) {
		String sql = "SELECT `soundtrack`.* , " + MUSIC_INFO_FIELDS + " "
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
				+ "ORDER BY `aus_order` ASC , `seq_order` ASC , `stk_order` ASC "
				+ getLimit(range);
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
				+ "LEFT JOIN `catalog` ON `cat_id` = `rel_catalog_id` "
				+ "LEFT JOIN `multimedium` ON `mm_id` = `rel_multimedia_id` "
				+ "LEFT JOIN `production` ON `prod_id` = `rel_production_id` "
				+ "LEFT JOIN `type` ON `typ_id` = `prod_type_id` "
				+ "LEFT JOIN `edition` ON `edt_id` = `prod_edition_id` "
				+ "LEFT JOIN `series` ON `ser_id` = `edt_series_id` "
				+ "WHERE `prod_edition_id` LIKE ? "
				+ "ORDER BY " + composePrintOrder(true) + " "
				+ getLimit(range);
		return sql;
	}
}

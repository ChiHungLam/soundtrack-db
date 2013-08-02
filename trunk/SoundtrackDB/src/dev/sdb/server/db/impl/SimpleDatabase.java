package dev.sdb.server.db.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gwt.view.client.Range;

import dev.sdb.server.db.SqlServer;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Genre;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public class SimpleDatabase extends AbstractDatabase {

	public SimpleDatabase(SqlServer sqlServer) {
		super(sqlServer);
	}

	@Override public void repairCatalogChildInfo() throws IOException {
		// TODO SimpleDatabase
	}

	@Override protected String composeCatalogReleaseList(Range range, boolean ascending) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeCatalogReleaseListCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeCatalogLevelList() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected Release readRelease(ResultSet rs) throws SQLException {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected Series readSeries(ResultSet rs) throws SQLException {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected Music readMusic(ResultSet rs) throws SQLException {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected Genre readGenre(ResultSet rs) throws SQLException {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected Catalog readCatalog(ResultSet rs) throws SQLException {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected Soundtrack readSoundtrack(ResultSet rs, boolean readRelease, boolean readMusic) throws SQLException {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeReleaseGet() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeMusicGet() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSoundtrackGet() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeCatalogGet() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeMusicReleaseList(Range range) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeMusicReleaseListCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeMusicCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeMusicList(Range range, boolean ascending) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSoundtrackList(Range range, boolean ascending) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeReleaseSoundtrackList(Range range) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeReleaseCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSoundtrackCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeReleaseSoundtrackListCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeReleaseList(Range range, boolean ascending) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSeriesGet() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSeriesList(Range range, boolean ascending) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSeriesCount() {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSeriesReleaseList(Range range) {
		// TODO SimpleDatabase
		return null;
	}

	@Override protected String composeSeriesReleaseListCount() {
		// TODO SimpleDatabase
		return null;
	}

}

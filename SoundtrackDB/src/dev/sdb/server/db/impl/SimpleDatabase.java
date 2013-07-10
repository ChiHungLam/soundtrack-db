package dev.sdb.server.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gwt.view.client.Range;

import dev.sdb.server.db.SqlServer;
import dev.sdb.shared.model.entity.Genre;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public class SimpleDatabase extends AbstractDatabase {

	public SimpleDatabase(SqlServer sqlServer) {
		super(sqlServer);
	}

	@Override protected Release readRelease(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected Series readSeries(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected Music readMusic(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected Genre readGenre(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected Soundtrack readSoundtrack(ResultSet rs, boolean readRelease, boolean readMusic) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeReleaseGet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeMusicGet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeSoundtrackGet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeMusicReleaseList(Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeMusicReleaseListCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeMusicCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeMusicList(Range range, boolean ascending) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeSoundtrackList(Range range, boolean ascending) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeReleaseSoundtrackList(Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeReleaseCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeSoundtrackCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeReleaseSoundtrackListCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override protected String composeReleaseList(Range range, boolean ascending) {
		// TODO Auto-generated method stub
		return null;
	}

}

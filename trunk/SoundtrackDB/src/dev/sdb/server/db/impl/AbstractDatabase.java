package dev.sdb.server.db.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import com.google.gwt.view.client.Range;

import dev.sdb.server.db.Database;
import dev.sdb.server.db.SqlManager;
import dev.sdb.server.db.SqlServer;
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Genre;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public abstract class AbstractDatabase extends SqlManager implements Database {

	public AbstractDatabase(SqlServer sqlServer) {
		super(sqlServer);
	}

	protected abstract Release readRelease(ResultSet rs) throws SQLException;

	protected abstract Series readSeries(ResultSet rs) throws SQLException;

	protected abstract Music readMusic(ResultSet rs) throws SQLException;

	protected abstract Genre readGenre(ResultSet rs) throws SQLException;

	protected abstract Soundtrack readSoundtrack(ResultSet rs, boolean readRelease, boolean readMusic) throws SQLException;

	protected abstract String composeReleaseGet();

	protected abstract String composeMusicGet();

	protected abstract String composeSoundtrackGet();

	protected abstract String composeMusicReleaseList(Range range);

	protected abstract String composeMusicReleaseListCount();

	protected abstract String composeMusicCount();

	protected abstract String composeMusicList(Range range, boolean ascending);

	protected abstract String composeSoundtrackList(Range range, boolean ascending);

	protected abstract String composeReleaseSoundtrackList(Range range);

	protected abstract String composeReleaseCount();

	protected abstract String composeSoundtrackCount();

	protected abstract String composeReleaseSoundtrackListCount();

	protected abstract String composeReleaseList(Range range, boolean ascending);

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

			listPS = getStatement(composeMusicReleaseList(range));
			countPS = getStatement(composeMusicReleaseListCount());
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

			listPS = getStatement(composeReleaseSoundtrackList(range));
			countPS = getStatement(composeReleaseSoundtrackListCount());
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
				listPS = getStatement(composeReleaseList(range, ascending));
				countPS = getStatement(composeReleaseCount());
				count = count(countPS, wildcardedTerm);
				queryReleases(listPS, result, wildcardedTerm, range, ascending);

				break;
			case MUSIC:
				listPS = getStatement(composeMusicList(range, ascending));
				countPS = getStatement(composeMusicCount());
				count = count(countPS, wildcardedTerm);
				queryMusic(listPS, result, wildcardedTerm, range, ascending);

				break;
			case SOUNDTRACK:
				listPS = getStatement(composeSoundtrackList(range, ascending));
				countPS = getStatement(composeSoundtrackCount());
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

	protected Soundtrack readSoundtrack(ResultSet rs) throws SQLException {
		return readSoundtrack(rs, true, true);
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

	protected String getOrderDirection(boolean ascending) {
		return ascending ? "ASC" : "DESC";
	}

	protected String getLimit(Range range) {
		return "LIMIT " + range.getStart() + " , " + range.getLength() + " ;";
	}





}
package dev.sdb.server.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public abstract class SqlManager implements Schema {

	private SqlServer sqlServer;
	private Connection connection;

	protected SqlManager(SqlServer sqlServer) {
		super();
		this.sqlServer = sqlServer;
	}

	protected String getDbName() {
		return this.sqlServer.getName();
	}

	public abstract void open() throws IOException;
	public abstract void close();

	protected void openConnection() throws IOException {
		this.connection = this.sqlServer.getConnection();
	}

	protected void closeConnection() {
		closeConnection(this.connection);
		this.connection = null;
	}

	protected void closeConnection(Connection connection) {
		if (connection == null)
			return;

		try {
			connection.close();
		} catch (SQLException e) {}
	}

	protected Connection getConnection() {
		return this.connection;
	}

	protected PreparedStatement getUpdatableStatement(String sql) throws SQLException {
		return getConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}

	protected PreparedStatement getStatement(String sql) throws SQLException {
		return getConnection().prepareStatement(sql);
	}

	protected PreparedStatement getInsertStatement(String sql, String columnName) throws SQLException {
		return getInsertStatement(sql, new String[] { columnName });
	}

	protected PreparedStatement getInsertStatement(String sql, String[] columnNames) throws SQLException {
		return getConnection().prepareStatement(sql, columnNames);
	}

	protected void closeStatement(PreparedStatement ps) {
		if (ps == null)
			return;

		try {
			ps.close();
		} catch (SQLException e) {}
	}

	protected void closeResultSet(ResultSet rs) {
		if (rs == null)
			return;

		try {
			rs.close();
		} catch (SQLException e) {}
	}

	protected long getGeneratedId(PreparedStatement ps) throws SQLException {
		ResultSet rs = null;
		try {
			rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getLong("GENERATED_KEY");
		} finally {
			closeResultSet(rs);
		}
	}

	public static java.sql.Date getSqlDate(Date date) {
		if (date == null)
			return null;
		return new java.sql.Date(date.getTime());
	}

}

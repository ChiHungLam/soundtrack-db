package dev.sdb.server.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class SqlServer {

	public static enum Property {
		DRIVER,
		PROTOCOL,
		SUB_PROTOCOL,
		HOST,
		PORT,
		DB,
		USER,
		PASSWORD,
		CHARACTER_ENCODING,
		USE_UNICODE
	}
	
	private boolean driverInitiated;
	private boolean connectionTested;

	private String db;
	private String user;
	private String password;

	private String url;

	public SqlServer() {
		super();
	}

	public String getName() {
		return this.db;
	}

	public static void checkParameters(Properties properties) throws IOException {
		for (Property property : Property.values()) {
			String value = properties.getProperty(property.name());
			if (value == null || value.isEmpty())
				throw new IOException("Property " + property.name() + " not contained in property set");
		}
	}

	public void init(Properties properties) throws IOException {
		this.driverInitiated = false;
		this.connectionTested = false;

		this.url = null;
		this.db = null;
		this.user = null;
		this.password = null;

		// parameter check
		checkParameters(properties);

		// caching properties
		String driverName = properties.getProperty(Property.DRIVER.name());
		String protocol = properties.getProperty(Property.PROTOCOL.name());
		String subProtocol = properties.getProperty(Property.SUB_PROTOCOL.name());
		String host = properties.getProperty(Property.HOST.name());
		String port = properties.getProperty(Property.PORT.name());
		String dbParam = properties.getProperty(Property.DB.name());
		String userParam = properties.getProperty(Property.USER.name());
		String passwordParam = properties.getProperty(Property.PASSWORD.name());
		String characterEncoding = properties.getProperty(Property.CHARACTER_ENCODING.name());
		boolean useUnicode = (properties.getProperty(Property.USE_UNICODE.name()).equalsIgnoreCase("true"));

		// init the given driver, may throw exception
		initDriver(driverName);
		this.driverInitiated = true;

		this.db = dbParam;
		this.user = userParam;
		this.password = passwordParam;

		// jdbc:mysql://localhost:3306/MailTool?useUnicode=yes&characterEncoding=UTF-8
		this.url = protocol + ":"
				+ subProtocol + "://"
				+ host + ":"
				+ port + "/"
				+ dbParam
				+ "?useUnicode=" + (useUnicode ? "yes" : "no")
				+ "&characterEncoding=" + characterEncoding;

		// testing the availability of the resource		
		testConnection();
		this.connectionTested = true;

	}

	private void initDriver(String driver) throws IOException {
		try {
			DriverManager.registerDriver((Driver) Class.forName(driver).newInstance());
		} catch (Exception e) {
			if (e instanceof IOException)
				throw (IOException) e;
			throw new IOException(e);
		}
	}

	public Connection getConnection() throws IOException {
		if (!this.driverInitiated)
			throw new IOException("Database driver not initiated");
		if (!this.connectionTested)
			throw new IOException("Untested database connection");

		return getConnection(this.url, this.user, this.password);
	}

	private Connection getConnection(String url, String user, String password) throws IOException {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	private void testConnection() throws IOException {
		Connection connection = null;
		try {
			connection = getConnection(this.url, this.user, this.password);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new IOException(e);
			}
		}
	}


}

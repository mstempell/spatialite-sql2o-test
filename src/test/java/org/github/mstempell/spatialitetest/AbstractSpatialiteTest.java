package org.github.mstempell.spatialitetest;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class AbstractSpatialiteTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String DATABASE_JDBC_URL = "jdbc:sqlite::memory:";
	private static final String SQL_CONNECTION_INIT = "SELECT load_extension('mod_spatialite.dll')";
	private static final String SQL_INIT_SPATIAL_METADATA = "SELECT InitSpatialMetadata()";

	protected DataSource dataSource;

	private void initDataSource() {

		SQLiteConfig config = new SQLiteConfig();
		config.enableLoadExtension(true);

		SQLiteConnectionPoolDataSource sqLiteDataSource = new SQLiteConnectionPoolDataSource();
		sqLiteDataSource.setUrl(DATABASE_JDBC_URL);
		sqLiteDataSource.setConfig(config);

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDataSource(sqLiteDataSource);
		hikariConfig.setConnectionInitSql(SQL_CONNECTION_INIT);
		// Using single connection pool partly to keep in memory database alive
		hikariConfig.setMaximumPoolSize(1);
		dataSource = new HikariDataSource(hikariConfig);
	}

	private void initDatabase() {

		java.sql.Connection connection = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();

			statement.execute(SQL_CONNECTION_INIT);
			statement.execute(SQL_INIT_SPATIAL_METADATA);
			statement.executeUpdate(TestUtils.getTextFromFile("spatialite-db-test-create_tables.sql"));
			statement.executeUpdate(TestUtils.getTextFromFile("spatialite-db-test-initial_insert.sql"));

			connection.commit();

		} catch (SQLException e) {
			logger.error("Cannot initialize database", e);
			try {
				connection.rollback();
			} catch (SQLException e2) {
				logger.error("Cannot rollback", e2);
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Cannot close connection", e);
			}
		}
	}

	@Before
	public void init() {
		
		initDataSource();
		initDatabase();
	}

	@After
	public void closeDataSource() {

		((HikariDataSource) dataSource).close();
	}
}

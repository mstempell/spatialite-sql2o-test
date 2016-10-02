package com.github.mstempell.spatialitetest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcSpatialiteTest extends AbstractSpatialiteTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void test_using_JDBC() {

		Assert.assertArrayEquals(Arrays.asList().toArray(), getTestQueryResults(dataSource, 40000).toArray());
		Assert.assertArrayEquals(Arrays.asList(2).toArray(), getTestQueryResults(dataSource, 41000).toArray());
		Assert.assertArrayEquals(Arrays.asList(2).toArray(), getTestQueryResults(dataSource, 550000).toArray());
		Assert.assertArrayEquals(Arrays.asList(2, 3).toArray(), getTestQueryResults(dataSource, 560000).toArray());
	}

	public List<Integer> getTestQueryResults(DataSource dataSource, int distance) {

		List<Integer> ids = null;
		java.sql.Connection connection = null;

		try {

			connection = dataSource.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(
					TestUtils.getTextFromFile("query.sql").replaceAll("\\:distance", "?"));
			preparedStatement.setInt(1, distance);
			ResultSet resultSet = preparedStatement.executeQuery();

			ids = new ArrayList<Integer>();
			while (resultSet.next()) {
				ids.add(resultSet.getInt("id"));
			}

		} catch (SQLException e) {
			logger.error("Error while querying", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Error while trying to close connection", e);
				}
			}
		}
		return ids;
	}
}

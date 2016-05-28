package org.github.mstempell.spatialitetest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Sql2oSpatialiteTest extends AbstractSpatialiteTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void test_using_Sql2o() {

		Sql2o sql2o = new Sql2o(dataSource);

		List<QueryResult> results = null;

		Assert.assertEquals(0, getTestQueryResults(sql2o, 40000).size());

		results = getTestQueryResults(sql2o, 41000);

		Assert.assertEquals(1, results.size());
		Assert.assertEquals(2, results.get(0).getId());
		Assert.assertTrue(results.get(0).getDistance() > 40000);
		Assert.assertTrue(results.get(0).getDistance() < 41000);

		Assert.assertEquals(1, getTestQueryResults(sql2o, 550000).size());

		results = getTestQueryResults(sql2o, 551000);

		Assert.assertEquals(2, results.size());
		Assert.assertEquals(3, results.get(1).getId());
		Assert.assertTrue(results.get(1).getDistance() > 550000);
		Assert.assertTrue(results.get(1).getDistance() < 551000);

		insertMarseilleLocation(sql2o);

		Assert.assertEquals(2, getTestQueryResults(sql2o, 660000).size());

		results = getTestQueryResults(sql2o, 662200);

		Assert.assertEquals(3, results.size());
		Assert.assertEquals(4, results.get(2).getId());
		Assert.assertTrue(results.get(2).getDistance() > 660000);
		Assert.assertTrue(results.get(2).getDistance() < 662200);
	}

	public class QueryResult {

		private int id;
		private double distance;

		public int getId() {

			return id;
		}

		public void setId(int id) {

			this.id = id;
		}

		public double getDistance() {

			return distance;
		}

		public void setDistance(double distance) {

			this.distance = distance;
		}
	}

	public List<QueryResult> getTestQueryResults(Sql2o sql2o, int distance) {

		try (Connection connection = sql2o.open()) {

			return connection.createQuery(TestUtils.getTextFromFile("query.sql")).addParameter("distance", distance)
					.executeAndFetch(QueryResult.class);
		}
	}

	public void insertMarseilleLocation(Sql2o sql2o) {

		try (Connection connection = sql2o.open()) {

			connection.createQuery(TestUtils.getTextFromFile("insert.sql")).addParameter("id", 4)
					.addParameter("name", "Marseille").addParameter("coordinate", "POINT(5.382878 43.284014)")
					.executeUpdate();
		}
	}
}

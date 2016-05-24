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
		
		Assert.assertEquals(0, getTestQueryResults(sql2o, 40000).size());
		
		Assert.assertEquals(1, getTestQueryResults(sql2o, 41000).size());
		Assert.assertEquals(2, getTestQueryResults(sql2o, 41000).get(0).intValue());

		Assert.assertEquals(1, getTestQueryResults(sql2o, 550000).size());
		
		Assert.assertEquals(2, getTestQueryResults(sql2o, 560000).size());
		Assert.assertEquals(3, getTestQueryResults(sql2o, 560000).get(1).intValue());
		
		insertMarseilleLocation(sql2o);
		
		Assert.assertEquals(2, getTestQueryResults(sql2o, 660000).size());

		Assert.assertEquals(3, getTestQueryResults(sql2o, 663000).size());
		Assert.assertEquals(4, getTestQueryResults(sql2o, 663000).get(2).intValue());
	}

	public List<Integer> getTestQueryResults(Sql2o sql2o, int distance) {

		try (Connection connection = sql2o.open()) {

			return connection
					.createQuery(TestUtils.getTextFromFile("spatialite-db-test-query.sql"))
					.addParameter("distance", distance)
					.executeAndFetch(Integer.class);
		}
	}

	public void insertMarseilleLocation(Sql2o sql2o) {

		try (Connection connection = sql2o.open()) {

			connection
					.createQuery(TestUtils.getTextFromFile("spatialite-db-test-insert.sql"))
					.addParameter("id", 4)
					.addParameter("name", "Marseille")
					.addParameter("coordinate", "POINT(5.382878 43.284014)")
					.executeUpdate();
		}
	}
}

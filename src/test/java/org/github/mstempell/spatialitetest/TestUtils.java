package org.github.mstempell.spatialitetest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public final class TestUtils {

	private TestUtils() {
	}

	public static List<String> getColumnValues(List<Map<String, Object>> results, String columnName) {
		List<String> values = new ArrayList<>();
		for (Map<String, Object> row : results) {
			values.add(row.get(columnName).toString());
		}
		return values;
	}

	public static List<String> getColumnValues(ResultSet resultSet, String columnName) throws SQLException {
		List<String> values = new ArrayList<>();
		while (resultSet.next()) {
			values.add(resultSet.getString(columnName));
		}
		return values;
	}

	public static String getTextFromFile(String sqlFile) {
		
		if (sqlFile == null) {
			return null;
		}

		try {
			return FileUtils.readFileToString(new File(TestUtils.class.getClassLoader().getResource(sqlFile).getFile()),
					Charset.forName("UTF-8"));
		} catch (IOException e) {
			return null;
		}
	}
}

package org.github.mstempell.spatialitetest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public final class TestUtils {

	private TestUtils() {
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

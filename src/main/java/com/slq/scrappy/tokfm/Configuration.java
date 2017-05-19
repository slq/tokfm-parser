package com.slq.scrappy.tokfm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class Configuration {

	private static final String PROPERTIES_FILE = "application.properties";
	private static volatile Properties properties;

	private Configuration() {
	}

	public static String getProperty(String property) {
		try {
			loadProperties();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String value = properties.getProperty(property);

		if (value == null) {
			throw new RuntimeException("Cannot find " + property + " in " + PROPERTIES_FILE);
		}

		if (isBlank(value)) {
			throw new RuntimeException("Property " + property + " is not set in " + PROPERTIES_FILE);
		}

		return value;
	}

	private static void loadProperties() throws IOException {
		if (properties == null) {
			synchronized (Configuration.class) {
				if (properties == null) {
					Properties properties = new Properties();
					try (InputStream propertiesStream = Configuration.class.getResourceAsStream("/" + PROPERTIES_FILE)) {
						properties.load(propertiesStream);
						Configuration.properties = properties;
					}
				}
			}
		}
	}
}

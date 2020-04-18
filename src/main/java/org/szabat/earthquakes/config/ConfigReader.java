package org.szabat.earthquakes.config;

import org.szabat.earthquakes.EarthquakesApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    public static Properties loadPropertiesFile(String fileName) {
        InputStream propertiesFile = EarthquakesApplication.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();

        try {
            if (propertiesFile == null) {
                throw new FileNotFoundException();
            }
            properties.load(propertiesFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

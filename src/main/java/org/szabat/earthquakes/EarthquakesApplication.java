package org.szabat.earthquakes;

import com.fasterxml.jackson.databind.JsonNode;
import org.szabat.earthquakes.config.ConfigReader;
import org.szabat.earthquakes.models.City;

import java.util.Properties;

public class EarthquakesApplication {

    public static void main(String[] args) {

        Properties properties = ConfigReader.loadPropertiesFile("application.properties");
        String url= "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";
        double latitude = Double.parseDouble((String)properties.get("latitude"));
        double longitude = Double.parseDouble((String)properties.get("longitude"));

        City city = new City(latitude, longitude);
        NearestEarthquakes nearestEarthquakes = new NearestEarthquakes();
        JsonNode dataApiJson = nearestEarthquakes.fetchDataFromApi(url);
        String listOfEarthquakes = nearestEarthquakes.calculateTenNearestEarthquakes(city, dataApiJson);
        System.out.println(listOfEarthquakes);
    }
}

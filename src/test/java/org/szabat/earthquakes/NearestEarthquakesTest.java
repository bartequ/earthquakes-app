package org.szabat.earthquakes;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.szabat.earthquakes.models.City;
import org.szabat.earthquakes.models.Earthquake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NearestEarthquakesTest {

    NearestEarthquakes nearestEarthquakes;

    @BeforeEach
    void setUp() {
        nearestEarthquakes = new NearestEarthquakes();
    }

    @Test
    @DisplayName("Calculate distance between two points")
    void testCalculateDistanceBetweenTwoCoordinates() {
        //given
        City city = new City(40.730610, -73.935242);
        Earthquake earthquake = new Earthquake("Test", 23.2193, -53.1817);

        //when
        int distance = nearestEarthquakes.calculateDistance(city.getLatitude(), earthquake.getLatitude(), city.getLongitude(), earthquake.getLongitude());

        //then
        assertEquals(2747, distance);
    }

    @Test
    @DisplayName("Calculate distance between two identical points")
    void testCalculateDistanceBetweenTwoSameCoordinates() {
        //given
        City city = new City(40.730610, -73.935242);
        Earthquake earthquake = new Earthquake("Test", 40.730610, -73.935242);

        //when
        int distance = nearestEarthquakes.calculateDistance(city.getLatitude(), earthquake.getLatitude(), city.getLongitude(), earthquake.getLongitude());

        //then
        assertEquals(0, distance);
    }

    @Test
    @DisplayName("Fetch data from API")
    void testFetchDataFromApi() {
        //given
        String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";

        //when
        JsonNode response = nearestEarthquakes.fetchDataFromApi(url);

        //then
        assertNotNull(response);
    }
}

package org.szabat.earthquakes;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.szabat.earthquakes.exceptions.CannotFetchEarthquakesData;
import org.szabat.earthquakes.exceptions.CannotReturnDataInJsonFormat;
import org.szabat.earthquakes.models.City;
import org.szabat.earthquakes.models.Earthquake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearestEarthquakes {

    public JsonNode fetchDataFromApi(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().value() != 200) {
                throw new CannotFetchEarthquakesData("Couldn't fetch data from external API. Status is different than 200.");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser((response.getBody()));
            JsonNode data = mapper.readTree(parser);
            return data.get("features");

        } catch(CannotFetchEarthquakesData | IOException e) {
            e.printStackTrace();
            throw new CannotReturnDataInJsonFormat("Cannot return data in established format.");
        }
    }

    public String calculateTenNearestEarthquakes(City city, JsonNode earthquakes)  {

        List<Earthquake> calculatedEarthquakes = new ArrayList<>();

        for (JsonNode earthquake : earthquakes) {
            String title = String.valueOf(earthquake.get("properties").get("title"));
            JsonNode coordinates = earthquake.get("geometry").get("coordinates");
            Earthquake earthquakeObj = new Earthquake(title, coordinates.get(0).asDouble(), coordinates.get(1).asDouble());
            int distance = calculateDistance(city.getLatitude(), earthquakeObj.getLatitude(), city.getLongitude(), earthquakeObj.getLongitude());
            earthquakeObj.setDistance(distance);
            calculatedEarthquakes.add(earthquakeObj);
        }

        calculatedEarthquakes = calculatedEarthquakes.stream()
                                                     .distinct()
                                                     .sorted(Comparator.comparing(Earthquake::getDistance))
                                                     .collect(Collectors.toList());

        String nearestEarthquakes = "";
        for (int i = 0; i < 10; i++) {
            Earthquake earthquakeCalculated = calculatedEarthquakes.get(i);
            String titleCut = earthquakeCalculated.getTitle();
            titleCut = titleCut.replace("\"", "");
            nearestEarthquakes = nearestEarthquakes.concat(titleCut.concat(" || ").concat(String.valueOf(earthquakeCalculated.getDistance())).concat("\n"));
        }

        return nearestEarthquakes;
    }

    public int calculateDistance(double lat1, double lat2, double lon1, double lon2) {
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        if (lon1 == lon2 && lat1 == lat2) {
            return 0;
        }

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        int r = 6371;

        return (int) (c * r);
    }
}

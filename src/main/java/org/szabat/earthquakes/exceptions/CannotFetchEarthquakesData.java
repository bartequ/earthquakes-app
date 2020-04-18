package org.szabat.earthquakes.exceptions;

public class CannotFetchEarthquakesData extends RuntimeException {

    public CannotFetchEarthquakesData(String message) {
        super(message);
    }
}

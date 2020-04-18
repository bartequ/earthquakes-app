package org.szabat.earthquakes.exceptions;

public class CannotReturnDataInJsonFormat extends RuntimeException {

    public CannotReturnDataInJsonFormat(String message) {
        super(message);
    }
}

package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class FlightServiceUnavailableException extends RuntimeException {
    public FlightServiceUnavailableException(String message) {
        super(message);
    }
}

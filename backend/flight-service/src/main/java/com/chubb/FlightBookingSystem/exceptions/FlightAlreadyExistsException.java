package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FlightAlreadyExistsException extends RuntimeException {
	public FlightAlreadyExistsException(String flightNumber) {
		super("Flight Number "+flightNumber+" already exists");
	}
}

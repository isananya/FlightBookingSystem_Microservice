package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessNotGrantedException extends RuntimeException{
	public AccessNotGrantedException(String message) {
        super(message);
    }
}

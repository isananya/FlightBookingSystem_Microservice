package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}


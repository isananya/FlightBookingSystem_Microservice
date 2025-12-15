package com.chubb.FlightBookingSystem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.chubb.FlightBookingSystem.exceptions.FlightServiceUnavailableException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FlightServiceUnavailableException.class)
    public ResponseEntity<String> handleFlightDown(FlightServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Flight Service unavailable.");
    }
}

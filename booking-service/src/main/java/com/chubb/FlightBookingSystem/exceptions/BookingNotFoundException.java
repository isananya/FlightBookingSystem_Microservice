package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String pnr) {
        super("Booking not found with PNR : " + pnr);
    }
}

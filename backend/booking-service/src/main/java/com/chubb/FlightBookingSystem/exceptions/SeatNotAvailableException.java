package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SeatNotAvailableException extends RuntimeException {
	public SeatNotAvailableException(String seatNumber, int scheduleId) {
		super("Seat Number " + seatNumber + " not available on flight " + scheduleId + ".");
	}
}

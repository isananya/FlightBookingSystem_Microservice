package com.chubb.FlightBookingSystem.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.chubb.FlightBookingSystem.model.Flight;

@ResponseStatus(HttpStatus.CONFLICT)
public class ScheduleAlreadyExistsException extends RuntimeException{
	public ScheduleAlreadyExistsException(Flight flight, LocalDate departureDate) {
		super("Schedule with Flight Number"+flight.getFlightNumber()+" already scheduled on "+departureDate);
	}
}

package com.chubb.FlightBookingSystem.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends RuntimeException{
	public ScheduleNotFoundException(int scheduleId) {
		super("Schedule with id "+scheduleId+" doesnt exist");
	}
}

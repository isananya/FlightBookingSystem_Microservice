package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ScheduleDTO {

	private int id;
    private String airlineName;
    private LocalDate departureDate;
    private int availableSeats;
    private int totalSeats;
    private float basePrice;
    private Set<String> bookedSeats;
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus = FlightStatus.SCHEDULED;
    private String flightNumber;
    
    public enum FlightStatus {
        SCHEDULED,
        DELAYED,
        CANCELLED,
        COMPLETED
    }
    
    public ScheduleDTO(Schedule schedule) {
	    this.id = schedule.getId();
	    this.airlineName = schedule.getAirlineName();
	    this.departureDate = schedule.getDepartureDate();
	    this.basePrice = schedule.getBasePrice();
	    this.totalSeats = schedule.getTotalSeats();
	    this.availableSeats = schedule.getAvailableSeats();
	    this.bookedSeats = schedule.getBookedSeats();
	    this.flightStatus = ScheduleDTO.FlightStatus.valueOf(schedule.getFlightStatus().name());
	    this.flightNumber = schedule.getFlight().getFlightNumber();
	}
}

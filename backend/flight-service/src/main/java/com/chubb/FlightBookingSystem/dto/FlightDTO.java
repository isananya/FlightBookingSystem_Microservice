package com.chubb.FlightBookingSystem.dto;

import java.time.Duration;
import java.time.LocalTime;

import com.chubb.FlightBookingSystem.model.Flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
	private String flightNumber;
    private String sourceAirport;
    private String destinationAirport;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Duration duration;
    
    public FlightDTO(Flight flight) {
        this.flightNumber = flight.getFlightNumber();
        this.sourceAirport = flight.getSourceAirport();
        this.destinationAirport = flight.getDestinationAirport();
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = flight.getArrivalTime();
        this.duration = flight.getDuration();
    }
}

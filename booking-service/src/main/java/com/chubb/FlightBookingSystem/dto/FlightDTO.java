package com.chubb.FlightBookingSystem.dto;

import java.time.Duration;
import java.time.LocalTime;

import lombok.Data;

@Data
public class FlightDTO {
    private String flightNumber;
    private String sourceAirport;
    private String destinationAirport;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Duration duration;
}

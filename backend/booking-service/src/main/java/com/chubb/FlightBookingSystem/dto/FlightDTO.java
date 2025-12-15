package com.chubb.FlightBookingSystem.dto;

import java.time.Duration;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private String flightNumber;
    private String sourceAirport;
    private String destinationAirport;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Duration duration;
}

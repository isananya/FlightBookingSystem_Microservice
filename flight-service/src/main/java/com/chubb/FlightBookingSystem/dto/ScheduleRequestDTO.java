package com.chubb.FlightBookingSystem.dto; 

import java.time.LocalDate;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequestDTO {
    private String airlineName; 
    private LocalDate departureDate;
    private float basePrice;
    private int totalSeats;
    private int availableSeats;
    
    private String flightNumber;
}
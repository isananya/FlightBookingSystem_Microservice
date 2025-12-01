package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Set<String> bookedSeats=new HashSet<>();
    private FlightStatus flightStatus = FlightStatus.SCHEDULED;
    private String flightNumber;
    
    public enum FlightStatus {
        SCHEDULED,
        DELAYED,
        CANCELLED,
        COMPLETED
    }
}

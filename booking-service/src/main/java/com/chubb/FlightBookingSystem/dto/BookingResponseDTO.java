package com.chubb.FlightBookingSystem.dto;

import java.util.HashSet;

import com.chubb.FlightBookingSystem.model.Ticket;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class BookingResponseDTO {
	private int id;
	private String pnr;
	private boolean roundTrip;
	
	private String departureSourceAirport;
	private String departureDestinationAirport;
	private String arrivalSourceAirport;
	private String arrivalDestinationAirport;
	
	private double totalAmount;
    private HashSet<Ticket> ticketSet;
    
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
}

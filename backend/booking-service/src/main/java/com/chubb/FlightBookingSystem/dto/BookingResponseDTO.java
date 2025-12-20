package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;
import java.util.HashSet;

import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.model.Ticket.TicketStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
	private int id;
	private String pnr;
	private boolean roundTrip;
	
	private String sourceAirport;
	private String destinationAirport;
	private LocalDate departureDate ;
	private LocalDate arrivalDate;
	private int passengerCount;
	 private TicketStatus status;
	
	private float totalAmount;
    private HashSet<Ticket> ticketSet;
}

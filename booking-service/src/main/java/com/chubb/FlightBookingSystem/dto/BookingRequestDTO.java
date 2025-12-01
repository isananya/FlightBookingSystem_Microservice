package com.chubb.FlightBookingSystem.dto;

import java.util.HashSet;

import com.chubb.FlightBookingSystem.model.Ticket;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingRequestDTO {
	@Email
	private String emailId;
	private boolean roundTrip;
	private int departureScheduleId;
	private Integer returnScheduleId;
	@Min(value=1)
	private int passengerCount;
	
	private HashSet<TicketRequestDTO> passengers;
}

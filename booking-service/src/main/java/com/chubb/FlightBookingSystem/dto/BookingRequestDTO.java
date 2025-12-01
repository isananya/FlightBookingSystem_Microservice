package com.chubb.FlightBookingSystem.dto;

import java.util.HashSet;

import com.chubb.FlightBookingSystem.model.Ticket;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookingRequestDTO {


	@Email
	private String emailId;
	
	private int departureScheduleId;
	
	private Integer returnScheduleId;
	
	private boolean roundTrip;
	
	@Min(value=1)
	private int passengerCount;
	
	private HashSet<TicketRequestDTO> passengers;
	
	public int getDepartureScheduleId() {
		return departureScheduleId;
	}

	public void setDepartureScheduleId(int departureScheduleId) {
		this.departureScheduleId = departureScheduleId;
	}

	public Integer getReturnScheduleId() {
		return returnScheduleId;
	}

	public void setReturnScheduleId(Integer returnScheduleId) {
		this.returnScheduleId = returnScheduleId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public boolean isRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

	public int getPassengerCount() {
		return passengerCount;
	}

	public void setPassengerCount(int passengerCount) {
		this.passengerCount = passengerCount;
	}

	public HashSet<TicketRequestDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(HashSet<TicketRequestDTO> passengers) {
		this.passengers = passengers;
	}


	
	
	
}

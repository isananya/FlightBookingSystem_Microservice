package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FlightSearchRequestDTO {
	
	@NotBlank
	private String sourceAirport;
	
	@NotBlank
	private String destinationAirport;
	
	private Boolean roundTrip;
	
	@FutureOrPresent
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate departureDate;
	
	@FutureOrPresent
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate returnDate;
	
	@Min(value=1)
	private int passengerCount;

	public String getSourceAirport() {
		return sourceAirport;
	}

	public void setSourceAirport(String sourceAirport) {
		this.sourceAirport = sourceAirport;
	}

	public String getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public boolean isRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(boolean roundTrip) {
		this.roundTrip = roundTrip;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public int getPassengerCount() {
		return passengerCount;
	}

	public void setPassengerCount(int passengerCount) {
		this.passengerCount = passengerCount;
	}
}

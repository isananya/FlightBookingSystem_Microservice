package com.chubb.FlightBookingSystem.model;

import java.time.Duration;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flight {
	
	@Id
	@NotBlank
	private String flightNumber;
	
	@NotBlank
	private String sourceAirport;
	
	@NotBlank
	private String destinationAirport;
	
	private LocalTime departureTime;
	
	private LocalTime arrivalTime;
	
	private Duration duration;
}

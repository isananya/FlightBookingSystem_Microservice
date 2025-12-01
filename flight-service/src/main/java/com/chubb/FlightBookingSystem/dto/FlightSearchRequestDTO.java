package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

	public boolean isRoundTrip() {
		return roundTrip;
	}

}

package com.chubb.FlightBookingSystem.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank
	private String airlineName;
	
	private LocalDate departureDate;
	
	private float basePrice;
	
	private int totalSeats;
	
	private int availableSeats;
	
	@ElementCollection
	private Set<String> bookedSeats=new HashSet<>();;
	
	@Enumerated(EnumType.STRING)
    private FlightStatus flightStatus = FlightStatus.SCHEDULED;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "flightNumber", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Flight flight;
	
	public enum FlightStatus {
        SCHEDULED,
        DELAYED,
        CANCELLED,
        COMPLETED
    }
	
	public Schedule() {}

	public Schedule(ScheduleRequestDTO dto, Flight flight) {
	    this.flight = flight; 
	    this.airlineName = dto.getAirlineName();
	    this.departureDate = dto.getDepartureDate();
	    this.basePrice = dto.getBasePrice();
	    this.totalSeats = dto.getTotalSeats();
	    this.availableSeats = dto.getAvailableSeats();
	}

	

	public Set<String> getBookedSeats() {
		return bookedSeats;
	}

	public void setBookedSeats(Set<String> bookedSeats) {
		this.bookedSeats = bookedSeats;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	public FlightStatus getFlightStatus() {
		return flightStatus;
	}

	public void setFlightStatus(FlightStatus flightStatus) {
		this.flightStatus = flightStatus;
	}
	
}

package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.chubb.FlightBookingSystem.model.Ticket.Gender;
import com.chubb.FlightBookingSystem.model.Ticket.MealOption;
import com.chubb.FlightBookingSystem.model.Ticket.TicketStatus;

public class TicketResponseDTO {
	private String firstName;
    private String lastName;
    private int age;
    private Gender gender;
    private String seatNumber;
    private MealOption mealOption;
    private TicketStatus status;
    private LocalDate date;
    private String fromAirport;
    private String toAirport;
    private LocalTime fromTime;
    private LocalTime toTime;
    
	public TicketResponseDTO(String firstName, String lastName, int age, Gender gender, String seatNumber,
			MealOption mealOption, TicketStatus ticketStatus, LocalDate date, String fromAirport, String toAirport,
			LocalTime fromTime, LocalTime toTime) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
		this.seatNumber = seatNumber;
		this.mealOption = mealOption;
		this.status = ticketStatus;
		this.date = date;
		this.fromAirport = fromAirport;
		this.toAirport = toAirport;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public MealOption getMealOption() {
		return mealOption;
	}
	public void setMealOption(MealOption mealOption) {
		this.mealOption = mealOption;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getFromAirport() {
		return fromAirport;
	}
	public void setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
	}
	public String getToAirport() {
		return toAirport;
	}
	public void setToAirport(String toAirport) {
		this.toAirport = toAirport;
	}
	public LocalTime getFromTime() {
		return fromTime;
	}
	public void setFromTime(LocalTime fromTime) {
		this.fromTime = fromTime;
	}
	public LocalTime getToTime() {
		return toTime;
	}
	public void setToTime(LocalTime toTime) {
		this.toTime = toTime;
	}
    
    
}

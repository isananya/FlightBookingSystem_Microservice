package com.chubb.FlightBookingSystem.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank
	private String firstName;
	
	private String lastName;
	
	@Min(value=0)
	private int age;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private String seatNumber;
	
	@Enumerated(EnumType.STRING)
	private MealOption mealOption;
	
	@Enumerated(EnumType.STRING)
	private TicketStatus status = TicketStatus.CONFIRMED;
	
	private int scheduleId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "booking_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Booking booking;
	
	public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
	
	public enum MealOption {
		VEG,
		NONVEG
	}
	
	public enum TicketStatus{
		CONFIRMED,
		CANCELLED
	}

	
	public Ticket() {
		super();
	}

	public Ticket(@NotBlank String firstName, String lastName, @Min(0) int age, Gender gender, String seatNumber,
			MealOption mealOption,int scheduleId, Booking booking) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
		this.seatNumber = seatNumber;
		this.mealOption = mealOption;
		this.booking = booking;
		this.scheduleId = scheduleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
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

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
}

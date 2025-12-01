package com.chubb.FlightBookingSystem.dto;

import com.chubb.FlightBookingSystem.model.Ticket.*;
import com.chubb.FlightBookingSystem.model.Ticket.MealOption;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class TicketRequestDTO {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    
    private String departureSeatNumber;
    private String returnSeatNumber;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Enumerated(EnumType.STRING)
	private MealOption mealOption;

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

	public String getDepartureSeatNumber() {
		return departureSeatNumber;
	}

	public void setDepartureSeatNumber(String departureSeatNumber) {
		this.departureSeatNumber = departureSeatNumber;
	}

	public String getReturnSeatNumber() {
		return returnSeatNumber;
	}

	public void setReturnSeatNumber(String returnSeatNumber) {
		this.returnSeatNumber = returnSeatNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public MealOption getMealOption() {
		return mealOption;
	}

	public void setMealOption(MealOption mealOption) {
		this.mealOption = mealOption;
	}
}

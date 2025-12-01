package com.chubb.FlightBookingSystem.dto;

import com.chubb.FlightBookingSystem.model.Ticket.*;
import com.chubb.FlightBookingSystem.model.Ticket.MealOption;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
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
}

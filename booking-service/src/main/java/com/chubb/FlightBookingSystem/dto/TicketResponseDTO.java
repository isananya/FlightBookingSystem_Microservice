package com.chubb.FlightBookingSystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.chubb.FlightBookingSystem.model.Ticket.Gender;
import com.chubb.FlightBookingSystem.model.Ticket.MealOption;
import com.chubb.FlightBookingSystem.model.Ticket.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
}

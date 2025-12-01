package com.chubb.FlightBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer>{
	List<Ticket> findByBooking(Booking booking);
}

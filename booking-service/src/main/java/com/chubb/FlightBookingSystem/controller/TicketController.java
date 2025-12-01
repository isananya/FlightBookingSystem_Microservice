package com.chubb.FlightBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;
import com.chubb.FlightBookingSystem.service.TicketService;

@RestController
public class TicketController {
	@Autowired
    private TicketService ticketService;

    @GetMapping("/ticket/{pnr}")
    public ResponseEntity<?> getTicketsByPnr(@PathVariable String pnr) {
    	List<TicketResponseDTO> response = ticketService.getTicketsByPnr(pnr);
        return ResponseEntity.ok(response);
    }
}

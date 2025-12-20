package com.chubb.FlightBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.BookingResponseDTO;
import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.service.BookingService;
import com.chubb.FlightBookingSystem.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {
	private final BookingService bookingService;
	private final TicketService ticketService;
	
	@Autowired	
	public BookingController(BookingService bookingService, TicketService ticketService) {
		super();
		this.bookingService = bookingService;
		this.ticketService = ticketService;
	}

	@PostMapping()
	public ResponseEntity<String> saveBooking(@RequestBody @Valid BookingRequestDTO request){
		String pnr = bookingService.addBooking(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(pnr);
	}
	
	@GetMapping("/history/{emailId}")
	public ResponseEntity<List<BookingResponseDTO>> getTicketHistory(@PathVariable String emailId) {
	    List<BookingResponseDTO> bookings = ticketService.getTicketsByEmail(emailId);
	    return ResponseEntity.ok(bookings);
	}
	
	@DeleteMapping("/cancel/{pnr}")
	public ResponseEntity<String> cancelBooking(@PathVariable String pnr) {
	    bookingService.cancelBooking(pnr);
	    return ResponseEntity.ok("");
	}
}

package com.chubb.FlightBookingSystem.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.CancellationNotAllowedException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.SeatNotAvailableException;
import com.chubb.FlightBookingSystem.feign.FlightClient;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.model.Ticket.TicketStatus;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private FlightClient flightClient;
	
	private float calculateTotalPrice(BookingRequestDTO request) {
	
		float departureFare = flightClient.getPrice(request.getDepartureScheduleId());
		float totalFare = departureFare;
		
		if(request.isRoundTrip() && request.getReturnScheduleId() != null){
			float returnFare = flightClient.getPrice(request.getReturnScheduleId());
			totalFare += returnFare;
		}
		
		return totalFare;
	}
	
	@Transactional
	public String addBooking(BookingRequestDTO request) {
		HashSet<TicketRequestDTO> passengers = request.getPassengers();
		for(TicketRequestDTO passenger: passengers) {
			if (flightClient.isSeatBooked(request.getDepartureScheduleId(), passenger.getDepartureSeatNumber())>0) {
				throw new SeatNotAvailableException(passenger.getDepartureSeatNumber(), request.getDepartureScheduleId());
			}
			
			if(request.isRoundTrip() && flightClient.isSeatBooked(request.getReturnScheduleId(), 
							passenger.getReturnSeatNumber())>0){
				throw new SeatNotAvailableException(passenger.getReturnSeatNumber(), request.getReturnScheduleId());
			}	
		}
		
		if(!flightClient.existsById(request.getDepartureScheduleId())) {
			throw new ScheduleNotFoundException(request.getDepartureScheduleId());
		}
		
		if(request.isRoundTrip() && !flightClient.existsById(request.getReturnScheduleId())) {
			throw new ScheduleNotFoundException(request.getReturnScheduleId());
		}
		
		Booking booking = new Booking(
				request.isRoundTrip(),
				request.getDepartureScheduleId(),
				request.getReturnScheduleId(),
				calculateTotalPrice(request),
				request.getEmailId(),
				request.getPassengerCount()	
		);
		
		bookingRepository.save(booking);
		
		ScheduleDTO departureSchedule = flightClient.getSchedule(request.getDepartureScheduleId());
		flightClient.decrementSeats(request.getDepartureScheduleId(), request.getPassengerCount());
		Set<String> bookedSeats = new HashSet<>();
		for(TicketRequestDTO passenger: passengers) {
			Ticket ticket = new Ticket(
					passenger.getFirstName(),
					passenger.getLastName(),
					passenger.getAge(),
					passenger.getGender(),
					passenger.getDepartureSeatNumber(),
					passenger.getMealOption(),
					request.getDepartureScheduleId(),
					booking
			);
			
			bookedSeats.add(passenger.getDepartureSeatNumber());
			
			ticketRepository.save(ticket);
		}
		
		flightClient.addSeats(request.getDepartureScheduleId(),bookedSeats);
		
		if(request.isRoundTrip()) {
			ScheduleDTO returnSchedule = flightClient.getSchedule(request.getReturnScheduleId());
			flightClient.decrementSeats(request.getReturnScheduleId(),request.getPassengerCount());
			Set<String> bookedSeats2 = new HashSet<>();
			for(TicketRequestDTO passenger: passengers) {
				Ticket ticket = new Ticket(
						passenger.getFirstName(),
						passenger.getLastName(),
						passenger.getAge(),
						passenger.getGender(),
						passenger.getReturnSeatNumber(),
						passenger.getMealOption(),
						request.getReturnScheduleId(),
						booking
				);
				
				flightClient.addSeats(request.getReturnScheduleId(),bookedSeats2);
				ticketRepository.save(ticket);
				
			}
		}
		
		return booking.getPnr();
		
	}
	
	@Transactional
	public void cancelBooking(String pnr) {
	    Booking booking = bookingRepository.findByPnr(pnr)
	            .orElseThrow(() -> new BookingNotFoundException(pnr));

	    List<Ticket> tickets = ticketRepository.findByBooking(booking);
	    
	    for (Ticket ticket : tickets) {
	        ScheduleDTO schedule = flightClient.getSchedule(ticket.getScheduleId());
	        if (schedule.getDepartureDate().isBefore(LocalDate.now().plusDays(1))) {
	            throw new CancellationNotAllowedException(
	                "Cannot cancel ticket " + ticket.getSeatNumber() + 
	                " for schedule " + schedule.getId() + 
	                ". Cancellation allowed only 24+ hours before departure."
	            );
	        }
	    }
	    
	    for (Ticket ticket : tickets) {
	    	ticket.setStatus(TicketStatus.CANCELLED);
	        ticketRepository.save(ticket);
	        
	        flightClient.decrementSeats(ticket.getScheduleId(), -1);
	        flightClient.removeSeat(ticket.getScheduleId(),ticket.getSeatNumber());
	    }
	    
	  
	    booking.setTotalAmount(0); 
	    bookingRepository.save(booking);
	}

}

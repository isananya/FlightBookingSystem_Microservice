package com.chubb.FlightBookingSystem.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.BookingResponseDTO;
import com.chubb.FlightBookingSystem.dto.FlightDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.feign.FlightClient;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.model.Ticket.TicketStatus;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketService {

    private BookingRepository bookingRepository;
    private TicketRepository ticketRepository;
    private FlightClient flightClient;
    
    @Autowired
    public TicketService(BookingRepository bookingRepository, TicketRepository ticketRepository,
			FlightClient flightClient) {
		super();
		this.bookingRepository = bookingRepository;
		this.ticketRepository = ticketRepository;
		this.flightClient = flightClient;
	}
    
    public List<TicketResponseDTO> getTicketsByPnr(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException(pnr));

        List<Ticket> tickets = ticketRepository.findByBooking(booking);

        return mapTicketsToResponse(tickets);
    }
    
    public List<BookingResponseDTO> getTicketsByEmail(String emailId) {
    	List<Booking> bookings = bookingRepository.findByEmailId(emailId);

        if (bookings.isEmpty()) {
            throw new BookingNotFoundException(emailId);
        }

        return bookings.stream()
                .map(this::mapToBookingResponseDTO)
                .toList();
    }

    private List<TicketResponseDTO> mapTicketsToResponse(List<Ticket> tickets) {
        return tickets.stream()
            .map(t -> {
                ScheduleDTO schedule = flightClient.getSchedule(t.getScheduleId());
                
                FlightDTO flight = null;
                if (schedule != null) {
                    flight = flightClient.getFlight(schedule.getFlightNumber());
                }

                return new TicketResponseDTO(
                        t.getFirstName(),
                        t.getLastName(),
                        t.getAge(),
                        t.getGender(),
                        t.getSeatNumber(),
                        t.getMealOption(),
                        t.getStatus(),
                        schedule != null ? schedule.getDepartureDate() : null,
                        (schedule != null && flight != null) ? flight.getSourceAirport() : null,
                        (schedule != null && flight != null) ? flight.getDestinationAirport() : null,
                        (schedule != null && flight != null) ? flight.getDepartureTime() : null,
                        (schedule != null && flight != null) ? flight.getArrivalTime() : null
                );
            }).toList();
    }
    
    private BookingResponseDTO mapToBookingResponseDTO(Booking booking) {

        ScheduleDTO departureSchedule = flightClient.getSchedule(booking.getDepartureScheduleId());
        
        ScheduleDTO returnSchedule = null;
        if (booking.isRoundTrip() && booking.getReturnScheduleId() != null) {
            returnSchedule = flightClient.getSchedule(booking.getReturnScheduleId());
        }

        HashSet<Ticket> tickets = new HashSet<>(
                ticketRepository.findByBooking(booking)
        );
        
        TicketStatus status = tickets.iterator().next().getStatus();

        return new BookingResponseDTO(
                booking.getId(),
                booking.getPnr(),
                booking.isRoundTrip(),
                
                flightClient.getFlight(departureSchedule.getFlightNumber()).getSourceAirport(),
                flightClient.getFlight(departureSchedule.getFlightNumber()).getDestinationAirport(),
                departureSchedule.getDepartureDate(),

                returnSchedule != null
                        ? returnSchedule.getDepartureDate()
                        : null,

                booking.getPassengerCount(),
                status,
                booking.getTotalAmount(),
                tickets
        );
    }

}
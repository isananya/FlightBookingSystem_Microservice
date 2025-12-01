package com.chubb.FlightBookingSystem.service;

import com.chubb.FlightBookingSystem.dto.FlightDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.feign.FlightClient;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private FlightClient flightClient;

    @InjectMocks
    private TicketService ticketService;

    private Booking booking;
    private Ticket ticket;
    private ScheduleDTO schedule;
    private FlightDTO flight;

    @BeforeEach
    void setUp() {
        booking = new Booking(false, 1, null, 5000, "test@example.com", 1);
        booking.setPnr("PNR123");
        ticket = new Ticket("John", "Doe", 30, Ticket.Gender.MALE, "A1", Ticket.MealOption.VEG, 1, booking);

        schedule = new ScheduleDTO(1, "TestAir", LocalDate.now().plusDays(5), 99, 100, 5000, Collections.singleton("A1"), ScheduleDTO.FlightStatus.SCHEDULED, "FL123");
        flight = new FlightDTO("FL123", "NYC", "LAX", LocalTime.of(10, 0), LocalTime.of(13, 0), Duration.ofHours(3));
    }

    @Test
    void whenGetTicketsByPnr_thenSuccess() {
        when(bookingRepository.findByPnr("PNR123")).thenReturn(Optional.of(booking));
        when(ticketRepository.findByBooking(booking)).thenReturn(Collections.singletonList(ticket));
        when(flightClient.getSchedule(1)).thenReturn(schedule);
        when(flightClient.getFlight("FL123")).thenReturn(flight);

        List<TicketResponseDTO> tickets = ticketService.getTicketsByPnr("PNR123");

        assertFalse(tickets.isEmpty());
        assertEquals(1, tickets.size());
        assertEquals("John", tickets.get(0).getFirstName());
    }

    @Test
    void whenGetTicketsByPnr_BookingNotFound_thenThrowException() {
        when(bookingRepository.findByPnr("PNR123")).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> ticketService.getTicketsByPnr("PNR123"));
    }

    @Test
    void whenGetTicketsByEmail_thenSuccess() {
        when(bookingRepository.findByEmailId("test@example.com")).thenReturn(Collections.singletonList(booking));
        when(ticketRepository.findByBooking(booking)).thenReturn(Collections.singletonList(ticket));
        when(flightClient.getSchedule(1)).thenReturn(schedule);
        when(flightClient.getFlight("FL123")).thenReturn(flight);

        List<TicketResponseDTO> tickets = ticketService.getTicketsByEmail("test@example.com");

        assertFalse(tickets.isEmpty());
        assertEquals(1, tickets.size());
        assertEquals("Doe", tickets.get(0).getLastName());
    }

    @Test
    void whenGetTicketsByEmail_BookingNotFound_thenThrowException() {
        when(bookingRepository.findByEmailId("test@example.com")).thenReturn(Collections.emptyList());

        assertThrows(BookingNotFoundException.class, () -> ticketService.getTicketsByEmail("test@example.com"));
    }
    
    @Test
    void whenGetTicketsByEmailWithNullSchedule_thenHandleGracefully() {
        when(bookingRepository.findByEmailId("test@example.com")).thenReturn(Collections.singletonList(booking));
        when(ticketRepository.findByBooking(booking)).thenReturn(Collections.singletonList(ticket));
        when(flightClient.getSchedule(1)).thenReturn(null);
    
        List<TicketResponseDTO> tickets = ticketService.getTicketsByEmail("test@example.com");
    
        assertFalse(tickets.isEmpty());
        assertEquals(1, tickets.size());
        assertNull(tickets.get(0).getDate());
        assertNull(tickets.get(0).getFromAirport());
    }
}

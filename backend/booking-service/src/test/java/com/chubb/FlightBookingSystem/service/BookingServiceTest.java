package com.chubb.FlightBookingSystem.service;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.BookingNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.CancellationNotAllowedException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.SeatNotAvailableException;
import com.chubb.FlightBookingSystem.feign.FlightClientWrapper;
import com.chubb.FlightBookingSystem.model.Booking;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.model.Ticket.Gender;
import com.chubb.FlightBookingSystem.model.Ticket.MealOption;
import com.chubb.FlightBookingSystem.repository.BookingRepository;
import com.chubb.FlightBookingSystem.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private FlightClientWrapper flightClient;

    @InjectMocks
    private BookingService bookingService;

    private BookingRequestDTO bookingRequest;
    private TicketRequestDTO passenger;
    private ScheduleDTO schedule;

    @BeforeEach
    void setUp() {
        passenger = new TicketRequestDTO("John", "Doe", 30, "A1", "B1", Gender.MALE, MealOption.VEG);
        Set<TicketRequestDTO> passengers = new HashSet<>();
        passengers.add(passenger);

        bookingRequest = new BookingRequestDTO("test@example.com", false, 1, null, 1, (HashSet<TicketRequestDTO>) passengers);
        schedule = new ScheduleDTO(1, "TestAir", LocalDate.now().plusDays(2), 100, 150, 5000, new HashSet<>(), ScheduleDTO.FlightStatus.SCHEDULED, "TA101");
    }

    @Test
    void whenAddBooking_OneWay_thenSuccess() {
        when(flightClient.isSeatBooked(anyInt(), anyString())).thenReturn(0);
        when(flightClient.existsById(1)).thenReturn(true);
        when(flightClient.getPrice(1)).thenReturn(5000f);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String pnr = bookingService.addBooking(bookingRequest);

        assertNotNull(pnr);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(flightClient, times(1)).decrementSeats(1, 1);
        verify(flightClient, times(1)).addSeats(eq(1), anySet());
    }

    @Test
    void whenAddBooking_RoundTrip_thenSuccess() {
        bookingRequest.setRoundTrip(true);
        bookingRequest.setReturnScheduleId(2);

        when(flightClient.isSeatBooked(anyInt(), anyString())).thenReturn(0);
        when(flightClient.existsById(1)).thenReturn(true);
        when(flightClient.existsById(2)).thenReturn(true);
        when(flightClient.getPrice(1)).thenReturn(5000f);
        when(flightClient.getPrice(2)).thenReturn(5500f);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String pnr = bookingService.addBooking(bookingRequest);

        assertNotNull(pnr);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(ticketRepository, times(2)).save(any(Ticket.class)); 
        verify(flightClient, times(1)).decrementSeats(1, 1);
        verify(flightClient, times(1)).decrementSeats(2, 1);
        verify(flightClient, times(1)).addSeats(eq(1), anySet());
    }

    @Test
    void whenAddBooking_SeatNotAvailable_thenThrowException() {
        when(flightClient.isSeatBooked(1, "A1")).thenReturn(1);

        assertThrows(SeatNotAvailableException.class, () -> bookingService.addBooking(bookingRequest));
    }

    @Test
    void whenAddBooking_ReturnSeatNotAvailable_thenThrowException() {
        bookingRequest.setRoundTrip(true);
        bookingRequest.setReturnScheduleId(2);
        when(flightClient.isSeatBooked(1, "A1")).thenReturn(0);
        when(flightClient.isSeatBooked(2, "B1")).thenReturn(1);

        assertThrows(SeatNotAvailableException.class, () -> bookingService.addBooking(bookingRequest));
    }


    @Test
    void whenAddBooking_DepartureScheduleNotFound_thenThrowException() {
        when(flightClient.isSeatBooked(anyInt(), anyString())).thenReturn(0);
        when(flightClient.existsById(1)).thenReturn(false);

        assertThrows(ScheduleNotFoundException.class, () -> bookingService.addBooking(bookingRequest));
    }

    @Test
    void whenAddBooking_ReturnScheduleNotFound_thenThrowException() {
        bookingRequest.setRoundTrip(true);
        bookingRequest.setReturnScheduleId(2);
        when(flightClient.isSeatBooked(anyInt(), anyString())).thenReturn(0);
        when(flightClient.existsById(1)).thenReturn(true);
        when(flightClient.existsById(2)).thenReturn(false);

        assertThrows(ScheduleNotFoundException.class, () -> bookingService.addBooking(bookingRequest));
    }

    @Test
    void whenCancelBooking_thenSuccess() {
        Booking booking = new Booking(false, 1, null, 5000, "test@example.com", 1);
        Ticket ticket = new Ticket("John", "Doe", 30, Gender.MALE, "A1", MealOption.VEG, 1, booking);
        when(bookingRepository.findByPnr("PNR123")).thenReturn(Optional.of(booking));
        when(ticketRepository.findByBooking(booking)).thenReturn(Collections.singletonList(ticket));
        when(flightClient.getSchedule(1)).thenReturn(schedule);

        bookingService.cancelBooking("PNR123");

        assertEquals(0, booking.getTotalAmount());
        assertEquals(Ticket.TicketStatus.CANCELLED, ticket.getStatus());
        verify(bookingRepository, times(1)).save(booking);
        verify(ticketRepository, times(1)).save(ticket);
        verify(flightClient, times(1)).decrementSeats(1, -1);
        verify(flightClient, times(1)).removeSeat(1, "A1");
    }

    @Test
    void whenCancelBooking_NotFound_thenThrowException() {
        when(bookingRepository.findByPnr("PNR123")).thenReturn(Optional.empty());
        assertThrows(BookingNotFoundException.class, () -> bookingService.cancelBooking("PNR123"));
    }

    @Test
    void whenCancelBooking_CancellationNotAllowed_thenThrowException() {
        Booking booking = new Booking(false, 1, null, 5000, "test@example.com", 1);
        Ticket ticket = new Ticket("John", "Doe", 30, Gender.MALE, "A1", MealOption.VEG, 1, booking);
        schedule.setDepartureDate(LocalDate.now()); 
        when(bookingRepository.findByPnr("PNR123")).thenReturn(Optional.of(booking));
        when(ticketRepository.findByBooking(booking)).thenReturn(Collections.singletonList(ticket));
        when(flightClient.getSchedule(1)).thenReturn(schedule);

        assertThrows(CancellationNotAllowedException.class, () -> bookingService.cancelBooking("PNR123"));
    }
}
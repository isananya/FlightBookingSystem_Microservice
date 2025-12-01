package com.chubb.FlightBookingSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.FlightBookingSystem.dto.FlightDTO;
import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.FlightNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.repository.FlightRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Flight flight;
    private Schedule schedule;
    private ScheduleRequestDTO scheduleRequestDTO;

    @BeforeEach
    void setUp() {
        flight = new Flight("FL123", "JFK", "LAX", null, null, null);
        scheduleRequestDTO = new ScheduleRequestDTO("Test Air", LocalDate.now(), 100.0f, 150, 150, "FL123");
        schedule = new Schedule(scheduleRequestDTO, flight);
        schedule.setId(1);
    }

    @Test
    void whenAddSchedule_withValidFlight_thenSaveSchedule() {
        when(flightRepository.findById("FL123")).thenReturn(Optional.of(flight));
        when(scheduleRepository.existsByFlight(any(Flight.class))).thenReturn(false);
        
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule result = scheduleService.addSchedule(scheduleRequestDTO);

        assertNotNull(result);
        assertEquals("FL123", result.getFlight().getFlightNumber());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void whenAddSchedule_withInvalidFlight_thenThrowFlightNotFoundException() {
        when(flightRepository.findById("FL123")).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> {
            scheduleService.addSchedule(scheduleRequestDTO);
        });
    }

    @Test
    void whenAddSchedule_thatAlreadyExists_thenThrowScheduleAlreadyExistsException() {
        when(flightRepository.findById("FL123")).thenReturn(Optional.of(flight));
        when(scheduleRepository.existsByFlight(any(Flight.class))).thenReturn(true);
        when(scheduleRepository.existsByDepartureDate(any(LocalDate.class))).thenReturn(true);

        assertThrows(ScheduleAlreadyExistsException.class, () -> {
            scheduleService.addSchedule(scheduleRequestDTO);
        });
    }

    @Test
    void whenSearchFlights_forOneWay_thenReturnsDepartureFlights() {
        FlightSearchRequestDTO request = new FlightSearchRequestDTO("JFK", "LAX", false, LocalDate.now(), null, 1);
        when(scheduleRepository.findFlights("JFK", "LAX", LocalDate.now(), 1)).thenReturn(List.of(schedule));

        Map<String, Object> results = scheduleService.searchFlights(request);

        assertNotNull(results);
        assertEquals(1, results.get("departureCount"));
        assertEquals(0, results.get("returnCount"));
        verify(scheduleRepository, times(1)).findFlights("JFK", "LAX", LocalDate.now(), 1);
    }

    @Test
    void whenSearchFlights_forRoundTrip_thenReturnsDepartureAndReturnFlights() {
        FlightSearchRequestDTO request = new FlightSearchRequestDTO("JFK", "LAX", true, LocalDate.now(), LocalDate.now().plusDays(2), 1);
        when(scheduleRepository.findFlights("JFK", "LAX", request.getDepartureDate(), 1)).thenReturn(List.of(schedule));
        when(scheduleRepository.findFlights("LAX", "JFK", request.getReturnDate(), 1)).thenReturn(List.of(schedule));

        Map<String, Object> results = scheduleService.searchFlights(request);

        assertNotNull(results);
        assertEquals(1, results.get("departureCount"));
        assertEquals(1, results.get("returnCount"));
        verify(scheduleRepository, times(1)).findFlights("JFK", "LAX", request.getDepartureDate(), 1);
        verify(scheduleRepository, times(1)).findFlights("LAX", "JFK", request.getReturnDate(), 1);
    }
    
    @Test
    void whenSearchFlights_forRoundTrip_butNoReturnDate_thenReturnsDepartureOnly() {
        FlightSearchRequestDTO request = new FlightSearchRequestDTO("JFK", "LAX", true, LocalDate.now(), null, 1);
        when(scheduleRepository.findFlights("JFK", "LAX", LocalDate.now(), 1)).thenReturn(List.of(schedule));

        Map<String, Object> results = scheduleService.searchFlights(request);

        assertNotNull(results);
        assertEquals(1, results.get("departureCount"));
        assertEquals(0, results.get("returnCount"));
        verify(scheduleRepository, times(1)).findFlights("JFK", "LAX", LocalDate.now(), 1);
     
        verify(scheduleRepository, never()).findFlights(eq("LAX"), eq("JFK"), any(LocalDate.class), anyInt());
    }

    @Test
    void whenGetScheduleById_thenReturnsScheduleDTO() {
        when(scheduleRepository.findById(1)).thenReturn(schedule);
        ScheduleDTO result = scheduleService.getScheduleById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void whenGetFlightById_thenReturnsFlightDTO() {
        when(flightRepository.findByFlightNumber("FL123")).thenReturn(flight);
        FlightDTO result = scheduleService.getFlightById("FL123");
        assertNotNull(result);
        assertEquals("FL123", result.getFlightNumber());
    }

    @Test
    void whenIsSeatBooked_thenReturnsCount() {
        when(scheduleRepository.isSeatBooked(1, "A1")).thenReturn(1);
        int result = scheduleService.isSeatBooked(1, "A1");
        assertEquals(1, result);
    }

    @Test
    void whenDecrementSeats_thenUpdatesAvailableSeats() {
        when(scheduleRepository.findById(1)).thenReturn(schedule);
        scheduleService.decrementSeats(1, 2);
        assertEquals(148, schedule.getAvailableSeats());
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void whenAddSeats_thenUpdatesBookedSeats() {
        when(scheduleRepository.findById(1)).thenReturn(schedule);
        Set<String> newSeats = new HashSet<>(Set.of("C1", "C2"));
        
        scheduleService.addSeats(1, newSeats);
        
        assertTrue(schedule.getBookedSeats().contains("C1"));
        assertTrue(schedule.getBookedSeats().contains("C2"));
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void whenRemoveSeat_thenUpdatesBookedSeats() {
        schedule.setBookedSeats(new HashSet<>(Set.of("A1", "A2")));
        when(scheduleRepository.findById(1)).thenReturn(schedule);
        
        scheduleService.removeSeat(1, "A1");
        
        assertTrue(schedule.getBookedSeats().contains("A2"));
        assertEquals(1, schedule.getBookedSeats().size());
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void whenExistsById_thenReturnsBoolean() {
        when(scheduleRepository.existsById(1)).thenReturn(true);
        boolean result = scheduleService.existsById(1);
        assertTrue(result);
    }

    @Test
    void whenGetPrice_thenReturnsFloat() {
        when(scheduleRepository.getPrice(1)).thenReturn(100.0f);
        float result = scheduleService.getPrice(1);
        assertEquals(100.0f, result);
    }
}
package com.chubb.FlightBookingSystem.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chubb.FlightBookingSystem.exceptions.FlightAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.repository.FlightRepository;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void whenAddFlight_thatDoesNotExist_thenFlightIsSaved() {
        // Arrange
        Flight flight = new Flight("FL123", "JFK", "LAX", null, null, null);
        when(flightRepository.existsByFlightNumber("FL123")).thenReturn(false);

        // Act
        flightService.addFlight(flight);

        // Assert
        verify(flightRepository, times(1)).existsByFlightNumber("FL123");
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void whenAddFlight_thatAlreadyExists_thenThrowException() {
        // Arrange
        Flight flight = new Flight("FL123", "JFK", "LAX", null, null, null);
        when(flightRepository.existsByFlightNumber("FL123")).thenReturn(true);

        // Act & Assert
        assertThrows(FlightAlreadyExistsException.class, () -> {
            flightService.addFlight(flight);
        });
        verify(flightRepository, times(1)).existsByFlightNumber("FL123");
        verify(flightRepository, times(0)).save(flight);
    }
}

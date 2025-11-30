package com.chubb.FlightBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.exceptions.FlightAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.repository.FlightRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FlightService {
	@Autowired
	private FlightRepository flightRepository;
	
	@Transactional
	public void addFlight(Flight flight) {
		if(flightRepository.existsByFlightNumber(flight.getFlightNumber())) {
			throw new FlightAlreadyExistsException(flight.getFlightNumber());
		}
		flightRepository.save(flight);
	}
}

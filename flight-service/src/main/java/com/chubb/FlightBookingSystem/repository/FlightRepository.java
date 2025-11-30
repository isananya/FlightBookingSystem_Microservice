package com.chubb.FlightBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String>{
	boolean existsByFlightNumber(String flightNumber);
}

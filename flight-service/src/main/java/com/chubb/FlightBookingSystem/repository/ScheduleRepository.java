package com.chubb.FlightBookingSystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
	boolean existsByFlight(Flight flight);
	boolean existsByDepartureDate(LocalDate departureDate);
	boolean existsById(int id);
	Schedule findById(int id);
	
	@Query("SELECT s FROM Schedule s "+
			"WHERE s.flight.sourceAirport = :src "+
			"AND s.flight.destinationAirport = :dest "+
			"AND s.departureDate = :date "+
			"AND s.availableSeats >= :passengerCount "+
			"AND s.flightStatus = 'SCHEDULED' "+
			"ORDER BY s.flight.departureTime ")
	List<Schedule> findFlights(
			@Param("src") String sourceAirport,
			@Param("dest") String destinationAirport,
			@Param("date") LocalDate travelDate,
			@Param("passengerCount") int passengerCount
	);
	@Query("SELECT count(s) FROM Schedule s WHERE s.id = :scheduleId AND :seatNumber MEMBER OF s.bookedSeats")
    int isSeatBooked(
    		@Param("scheduleId") int scheduleId, 
    		@Param("seatNumber") String seatNumber
    );
}

package com.chubb.FlightBookingSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chubb.FlightBookingSystem.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{

	Optional<Booking> findByPnr(String pnr);

	List<Booking> findByEmailId(String emailId);
}

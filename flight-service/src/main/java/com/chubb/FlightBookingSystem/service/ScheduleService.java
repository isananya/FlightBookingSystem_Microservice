package com.chubb.FlightBookingSystem.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleResponseDTO;
import com.chubb.FlightBookingSystem.exceptions.FlightNotFoundException;
import com.chubb.FlightBookingSystem.exceptions.ScheduleAlreadyExistsException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.repository.FlightRepository;
import com.chubb.FlightBookingSystem.repository.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduleService {
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Transactional
	public Schedule addSchedule(ScheduleRequestDTO scheduleDto) {
		String flightNumber = scheduleDto.getFlightNumber();
		
		Flight flight = flightRepository.findById(flightNumber)
		        .orElseThrow(() -> new FlightNotFoundException(flightNumber));
		
		Schedule schedule = new Schedule(scheduleDto,flight);
		
		if(scheduleRepository.existsByFlight(schedule.getFlight()) &&
				scheduleRepository.existsByDepartureDate(schedule.getDepartureDate())) {
			throw new ScheduleAlreadyExistsException(schedule.getFlight(), schedule.getDepartureDate());
		}
		return scheduleRepository.save(schedule);
	}
	
	private ScheduleResponseDTO mapToResponseDTO(Schedule schedule) {
        Flight flight = schedule.getFlight();
        
        ScheduleResponseDTO dto = new ScheduleResponseDTO();
        dto.setDepartureDate(schedule.getDepartureDate());
        dto.setBasePrice(schedule.getBasePrice());
        dto.setAirlineName(schedule.getAirlineName());
        
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setSourceAirport(flight.getSourceAirport());
        dto.setDestinationAirport(flight.getDestinationAirport());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setDuration(flight.getDuration());
        
        return dto;
    }
	
	@Transactional	
	public Map<String, Object> searchFlights(FlightSearchRequestDTO request){
		List<Schedule> departureSchedules = scheduleRepository.findFlights(
	            request.getSourceAirport(),
	            request.getDestinationAirport(),
	            request.getDepartureDate(),
	            request.getPassengerCount()
	    );
		
		List<ScheduleResponseDTO> departureResults = departureSchedules.stream()
				.map(this::mapToResponseDTO)
	            .collect(Collectors.toList());
		
		Map<String, Object> results = new HashMap<>();
        results.put("departure", departureResults);
        results.put("departureCount", departureResults.size());
        
        if (request.isRoundTrip() && request.getReturnDate() != null) {
	        List<Schedule> returnSchedules = scheduleRepository.findFlights(
	                request.getDestinationAirport(),
	                request.getSourceAirport(),
	                request.getReturnDate(),
	                request.getPassengerCount()
	        );
	  
	        
	        List<ScheduleResponseDTO> returnResults = returnSchedules.stream()
					.map(this::mapToResponseDTO)
		            .collect(Collectors.toList());
	        
	        results.put("return", returnResults);
	        results.put("returnCount", returnResults.size());
        } 
        else {
            results.put("return", Collections.emptyList());
            results.put("returnCount", 0);            
        }
        
        return results;
	}
}

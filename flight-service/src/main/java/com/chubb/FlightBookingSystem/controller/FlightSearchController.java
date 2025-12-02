package com.chubb.FlightBookingSystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.FlightDTO;
import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.service.ScheduleService;

@RestController
public class FlightSearchController {
	private final ScheduleService scheduleService;

	@Autowired
	public FlightSearchController(ScheduleService scheduleService) {
		super();
		this.scheduleService = scheduleService;
	}	
	
	@GetMapping("/flights/search")
	public ResponseEntity<Map<String, Object>> searchFlights(
			@ModelAttribute FlightSearchRequestDTO request) {
		if (request.getSourceAirport() == null || 
				request.getDestinationAirport() == null || 
				request.getDepartureDate() == null ) {
             return ResponseEntity.badRequest().build();
        }
        Map<String, Object> results = scheduleService.searchFlights(request);
        
        return ResponseEntity.ok(results);
    }
	
	@GetMapping("/flight/{id}")
	public ResponseEntity<FlightDTO> getFlight(@PathVariable("id") String flightNumber) {
		FlightDTO dto = scheduleService.getFlightById(flightNumber);
		return ResponseEntity.ok(dto);
	}

}

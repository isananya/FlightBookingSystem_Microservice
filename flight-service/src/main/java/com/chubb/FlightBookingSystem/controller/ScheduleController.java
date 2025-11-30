package com.chubb.FlightBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.AccessNotGrantedException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.service.FlightService;
import com.chubb.FlightBookingSystem.service.ScheduleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/airline")
public class ScheduleController {
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	FlightService flightService;
	private final String adminSecretKey = "Admin";
	
	@PostMapping("/inventory")
	public ResponseEntity<String> saveSchedule(@RequestHeader(value="Admin_key",required=false) String adminKey,
			@RequestBody @Valid ScheduleRequestDTO scheduleDto) {
		System.out.println(adminKey+" : "+adminSecretKey);
		if(adminKey==null || !adminSecretKey.equals(adminKey)) {
			throw new AccessNotGrantedException("Access denied: Invalid or missing Admin Key");
		}
		Schedule schedule = scheduleService.addSchedule(scheduleDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Schedule added!!");
	}
	
	@PostMapping("/route")
	public ResponseEntity<String> saveFlight(@RequestHeader(value="Admin_key",required=false) String adminKey,
			@RequestBody @Valid Flight flight){
		if(adminKey==null || !adminSecretKey.equals(adminKey)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		flightService.addFlight(flight);
		return ResponseEntity.status(HttpStatus.CREATED).body("Flight details added!!");
	}
}

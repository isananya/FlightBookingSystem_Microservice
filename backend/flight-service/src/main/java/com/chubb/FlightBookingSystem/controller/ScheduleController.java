package com.chubb.FlightBookingSystem.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.exceptions.AccessNotGrantedException;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.service.FlightService;
import com.chubb.FlightBookingSystem.service.ScheduleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final FlightService flightService;
	
	@Autowired	
	public ScheduleController(ScheduleService scheduleService, FlightService flightService) {
		super();
		this.scheduleService = scheduleService;
		this.flightService = flightService;
	}
	
	@PostMapping("/inventory")
	public ResponseEntity<Integer> saveSchedule(@RequestBody @Valid ScheduleRequestDTO scheduleDto) {
		Schedule schedule = scheduleService.addSchedule(scheduleDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(schedule.getId());
	}
	
	@PostMapping("/route")
	public ResponseEntity<String> saveFlight(@RequestBody @Valid Flight flight){
		flightService.addFlight(flight);
		return ResponseEntity.status(HttpStatus.CREATED).body(flight.getFlightNumber());
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable int id) {
        ScheduleDTO dto = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/seat/check")
    public int isSeatBooked(@RequestParam int scheduleId,
                            @RequestParam String seatNumber) {
        return scheduleService.isSeatBooked(scheduleId, seatNumber);
    }

    @PutMapping("/{id}/seat/decrement")
    public void decrementSeats(@PathVariable int id,
                               @RequestParam int count) {
        scheduleService.decrementSeats(id, count);
    }

    @GetMapping("/{id}/check")
    public boolean existsById(@PathVariable int id) {
        return scheduleService.existsById(id);
    }

    @PutMapping("/{id}/seat/add")
    public void addSeats(@PathVariable int id,
    		@RequestBody Set<String> seats) {
        scheduleService.addSeats(id,seats);
    }
    
    @PutMapping("/{id}/seat/remove")
    public void removeSeat(@PathVariable int id,
    		@RequestBody String seat) {
        scheduleService.removeSeat(id,seat);
    }
    
    @GetMapping("/{id}/price")
    public float getPrice(@PathVariable int id) {
    	float price = scheduleService.getPrice(id);
    	return price;
    }
}

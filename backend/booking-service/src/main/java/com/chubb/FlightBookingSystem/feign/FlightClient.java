package com.chubb.FlightBookingSystem.feign;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.chubb.FlightBookingSystem.dto.FlightDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleDTO;

@FeignClient(name = "flight-service")
public interface FlightClient {

    @GetMapping("/schedule/{id}")
    public ScheduleDTO getSchedule(@PathVariable("id") int id);

    @GetMapping("/schedule/seat/check")
    public int isSeatBooked(
            @RequestParam("scheduleId") int scheduleId,
            @RequestParam("seatNumber") String seatNumber
    );

    @PutMapping("/schedule/{id}/seat/decrement")
    void decrementSeats(@PathVariable("id") int scheduleId,
                        @RequestParam("count") int count);
    
    @PutMapping("/schedule/{id}/seat/add")
    void addSeats(@PathVariable("id") int scheduleId,
    				 @RequestBody Set<String> seats);
    
    @PutMapping("/schedule/{id}/seat/remove")
    void removeSeat(@PathVariable("id") int scheduleId,
    				 @RequestBody String seat);
    
    @GetMapping("/schedule/{id}/check")
    boolean existsById(@PathVariable("id") int scheduleId);

    @GetMapping("/schedule/{id}/price")
    float getPrice(@PathVariable("id") int scheduleId);
    
    @GetMapping("/flight/{id}")
    public FlightDTO getFlight(@PathVariable("id") String flightNumber);
}

package com.chubb.FlightBookingSystem.feign;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.exceptions.FlightServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class FlightClientWrapper {

    private final FlightClient flightClient;

    @Autowired
    public FlightClientWrapper(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "scheduleFallback")
    public ScheduleDTO getSchedule(int id) {
        return flightClient.getSchedule(id);
    }

    public ScheduleDTO scheduleFallback(int id, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "seatCheckFallback")
    public int isSeatBooked(int scheduleId, String seat) {
        return flightClient.isSeatBooked(scheduleId, seat);
    }

    public int seatCheckFallback(int scheduleId, String seat, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "decrementFallback")
    public void decrementSeats(int scheduleId, int count) {
        flightClient.decrementSeats(scheduleId, count);
    }

    public void decrementFallback(int scheduleId, int count, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "addSeatsFallback")
    public void addSeats(int scheduleId, Set<String> seats) {
        flightClient.addSeats(scheduleId, seats);
    }

    public void addSeatsFallback(int scheduleId, Set<String> seats, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "removeSeatFallback")
    public void removeSeat(int scheduleId, String seat) {
        flightClient.removeSeat(scheduleId, seat);
    }

    public void removeSeatFallback(int scheduleId, String seat, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "existsFallback")
    public boolean existsById(int scheduleId) {
        return flightClient.existsById(scheduleId);
    }

    public boolean existsFallback(int scheduleId, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }

    @CircuitBreaker(name = "flightServiceCB", fallbackMethod = "priceFallback")
    public float getPrice(int scheduleId) {
        return flightClient.getPrice(scheduleId);
    }

    public float priceFallback(int scheduleId, Throwable ex) {
        throw new FlightServiceUnavailableException("Flight service unavailable");
    }
}

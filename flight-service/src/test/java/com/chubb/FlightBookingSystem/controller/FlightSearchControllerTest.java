package com.chubb.FlightBookingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.chubb.FlightBookingSystem.dto.FlightDTO;
import com.chubb.FlightBookingSystem.dto.FlightSearchRequestDTO;
import com.chubb.FlightBookingSystem.service.ScheduleService;

@ExtendWith(MockitoExtension.class)
class FlightSearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private FlightSearchController flightSearchController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(flightSearchController).build();
    }

    @Test
    void whenSearchFlights_withValidRequest_thenReturnsOk() throws Exception {
        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("departureCount", 1);
        when(scheduleService.searchFlights(any(FlightSearchRequestDTO.class))).thenReturn(serviceResult);

        mockMvc.perform(get("/flights/search")
                .param("sourceAirport", "JFK")
                .param("destinationAirport", "LAX")
                .param("departureDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departureCount").value(1));
    }

    @Test
    void whenSearchFlights_withMissingParams_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/flights/search")
                .param("sourceAirport", "JFK"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetFlight_withValidId_thenReturnsOk() throws Exception {
        FlightDTO flightDTO = new FlightDTO("FL123", "JFK", "LAX", null, null, null);
        when(scheduleService.getFlightById("FL123")).thenReturn(flightDTO);

        mockMvc.perform(get("/flight/FL123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("FL123"));
    }
}
package com.chubb.FlightBookingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.chubb.FlightBookingSystem.dto.ScheduleDTO;
import com.chubb.FlightBookingSystem.dto.ScheduleRequestDTO;
import com.chubb.FlightBookingSystem.model.Flight;
import com.chubb.FlightBookingSystem.model.Schedule;
import com.chubb.FlightBookingSystem.service.FlightService;
import com.chubb.FlightBookingSystem.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private ScheduleController scheduleController;

    private final String ADMIN_KEY = "Admin";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void whenSaveSchedule_withValidAdminKey_thenReturnsCreated() throws Exception {
        ScheduleRequestDTO requestDTO = new ScheduleRequestDTO("Test Air", LocalDate.now(), 100f, 150, 150, "FL123");
        Schedule schedule = new Schedule();
        when(scheduleService.addSchedule(any(ScheduleRequestDTO.class))).thenReturn(schedule);

        mockMvc.perform(post("/schedule/inventory")
                .header("Admin_key", ADMIN_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Schedule added!!"));
    }

    @Test
    void whenSaveSchedule_withInvalidAdminKey_thenThrowsAccessNotGrantedException() throws Exception {
        ScheduleRequestDTO requestDTO = new ScheduleRequestDTO("Test Air", LocalDate.now(), 100f, 150, 150, "FL123");

        mockMvc.perform(post("/schedule/inventory")
                .header("Admin_key", "InvalidKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void whenSaveSchedule_withMissingAdminKey_thenThrowsAccessNotGrantedException() throws Exception {
        ScheduleRequestDTO requestDTO = new ScheduleRequestDTO("Test Air", LocalDate.now(), 100f, 150, 150, "FL123");
        
        mockMvc.perform(post("/schedule/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenSaveFlight_withValidAdminKey_thenReturnsCreated() throws Exception {
        Flight flight = new Flight("FL456", "DEN", "SFO", null, null, null);
        doNothing().when(flightService).addFlight(any(Flight.class));

        mockMvc.perform(post("/schedule/route")
                .header("Admin_key", ADMIN_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Flight details added!!"));
    }

    @Test
    void whenSaveFlight_withInvalidAdminKey_thenReturnsForbidden() throws Exception {
        Flight flight = new Flight("FL456", "DEN", "SFO", null, null, null);
        mockMvc.perform(post("/schedule/route")
                .header("Admin_key", "WrongKey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenGetSchedule_withValidId_thenReturnsOk() throws Exception {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1);
        when(scheduleService.getScheduleById(1)).thenReturn(scheduleDTO);

        mockMvc.perform(get("/schedule/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void whenIsSeatBooked_thenReturnsCount() throws Exception {
        when(scheduleService.isSeatBooked(1, "A1")).thenReturn(1);
        mockMvc.perform(get("/schedule/seat/check")
                .param("scheduleId", "1")
                .param("seatNumber", "A1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void whenDecrementSeats_thenReturnsOk() throws Exception {
        doNothing().when(scheduleService).decrementSeats(1, 2);
        mockMvc.perform(put("/schedule/1/seat/decrement")
                .param("count", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void whenExistsById_thenReturnsBoolean() throws Exception {
        when(scheduleService.existsById(1)).thenReturn(true);
        mockMvc.perform(get("/schedule/1/check"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void whenAddSeats_thenReturnsOk() throws Exception {
        Set<String> seats = Set.of("D1", "D2");
        doNothing().when(scheduleService).addSeats(anyInt(), any(Set.class));
        mockMvc.perform(put("/schedule/1/seat/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seats)))
                .andExpect(status().isOk());
    }

    @Test
    void whenRemoveSeat_thenReturnsOk() throws Exception {
        doNothing().when(scheduleService).removeSeat(anyInt(), anyString());
        mockMvc.perform(put("/schedule/1/seat/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("A1")))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetPrice_thenReturnsPrice() throws Exception {
        when(scheduleService.getPrice(1)).thenReturn(150.5f);
        mockMvc.perform(get("/schedule/1/price"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.5"));
    }
}
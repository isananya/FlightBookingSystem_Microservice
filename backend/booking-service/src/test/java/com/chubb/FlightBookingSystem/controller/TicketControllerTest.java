package com.chubb.FlightBookingSystem.controller;

import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock // Use standard Mockito annotation
    private TicketService ticketService;

    @InjectMocks // Automatically injects the mocked service into the controller
    private TicketController ticketController;

    @BeforeEach
    void setup() {
        // Manually build MockMvc with the controller instance
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    void whenGetTicketsByPnr_thenReturnsOk() throws Exception {
        TicketResponseDTO ticketResponse = new TicketResponseDTO();
        ticketResponse.setFirstName("Jane");
        ticketResponse.setLastName("Doe");
        List<TicketResponseDTO> tickets = Collections.singletonList(ticketResponse);

        when(ticketService.getTicketsByPnr("PNR456")).thenReturn(tickets);

        mockMvc.perform(get("/ticket/{pnr}", "PNR456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }
}
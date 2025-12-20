package com.chubb.FlightBookingSystem.controller;

import com.chubb.FlightBookingSystem.dto.BookingRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketRequestDTO;
import com.chubb.FlightBookingSystem.dto.TicketResponseDTO;
import com.chubb.FlightBookingSystem.model.Ticket;
import com.chubb.FlightBookingSystem.service.BookingService;
import com.chubb.FlightBookingSystem.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock // Use standard Mockito Mock
    private BookingService bookingService;

    @Mock // Use standard Mockito Mock
    private TicketService ticketService;

    @InjectMocks // Inject the mocks into the controller manually
    private BookingController bookingController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // This initializes MockMvc without needing the Spring Context
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void whenSaveBooking_thenReturnsCreated() throws Exception {
        HashSet<TicketRequestDTO> passengers = new HashSet<>();
        // Note: Using Ticket.Gender/MealOption assuming your imports are correct
        passengers.add(new TicketRequestDTO("John", "Doe", 30, "A1", null, Ticket.Gender.MALE, Ticket.MealOption.VEG));
        BookingRequestDTO request = new BookingRequestDTO("test@example.com", false, 1, null, 1, passengers);

        when(bookingService.addBooking(any(BookingRequestDTO.class))).thenReturn("PNR123");

        mockMvc.perform(post("/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Booking Successful! PNR : PNR123"));
    }

//    @Test
//    void whenGetTicketHistory_thenReturnsOk() throws Exception {
//        TicketResponseDTO ticketResponse = new TicketResponseDTO();
//        ticketResponse.setFirstName("John");
//        List<TicketResponseDTO> tickets = Collections.singletonList(ticketResponse);
//
//        when(ticketService.getTicketsByEmail("test@example.com")).thenReturn(tickets);
//
//        mockMvc.perform(get("/booking/history/{emailId}", "test@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].firstName").value("John"));
//    }

    @Test
    void whenCancelBooking_thenReturnsOk() throws Exception {
        doNothing().when(bookingService).cancelBooking("PNR123");

        mockMvc.perform(delete("/booking/cancel/{pnr}", "PNR123"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
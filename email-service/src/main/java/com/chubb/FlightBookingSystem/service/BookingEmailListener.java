package com.chubb.FlightBookingSystem.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.chubb.FlightBookingSystem.event.BookingEvent;
import com.chubb.FlightBookingSystem.service.EmailService;

@Service
public class BookingEmailListener {

    private final EmailService emailService;

    public BookingEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${app.queue.booking}")
    public void handleBookingEvent(BookingEvent event) {

        if ("BOOKING_CREATED".equals(event.getEventType())) {
            emailService.sendBookingEmail(event.getEmailId(), event.getPnr());
        } else if ("BOOKING_CANCELLED".equals(event.getEventType())) {
            emailService.sendCancellationEmail(event.getEmailId(), event.getPnr());
        }
    }
}


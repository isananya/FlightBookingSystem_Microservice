package com.chubb.FlightBookingSystem.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BookingEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String createdRoutingKey;
    private final String cancelledRoutingKey;

    public BookingEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.queue.exchange}") String exchange,
            @Value("${app.queue.routing.created}") String createdRoutingKey,
            @Value("${app.queue.routing.cancelled}") String cancelledRoutingKey) {

        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.createdRoutingKey = createdRoutingKey;
        this.cancelledRoutingKey = cancelledRoutingKey;
    }

    public void publishBookingCreated(String pnr, String emailId) {
        BookingEvent event = new BookingEvent("BOOKING_CREATED", pnr, emailId);
        rabbitTemplate.convertAndSend(exchange, createdRoutingKey, event);
    }

    public void publishBookingCancelled(String pnr, String emailId) {
        BookingEvent event = new BookingEvent("BOOKING_CANCELLED", pnr, emailId);
        rabbitTemplate.convertAndSend(exchange, cancelledRoutingKey, event);
    }
}
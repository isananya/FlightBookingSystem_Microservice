package com.chubb.FlightBookingSystem.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitConfig {

    @Value("${app.queue.booking}")
    private String bookingQueueName;

    @Value("${app.queue.exchange}")
    private String exchangeName;

    @Value("${app.queue.routing.created}")
    private String createdRoutingKey;

    @Value("${app.queue.routing.cancelled}")
    private String cancelledRoutingKey;

    @Bean
    public Queue bookingQueue() {
        return new Queue(bookingQueueName, true);
    }

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding bookingCreatedBinding(Queue bookingQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(bookingQueue)
                .to(bookingExchange)
                .with(createdRoutingKey);
    }

    @Bean
    public Binding bookingCancelledBinding(Queue bookingQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(bookingQueue)
                .to(bookingExchange)
                .with(cancelledRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}

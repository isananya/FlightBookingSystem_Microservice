package com.chubb.FlightBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingEmail(String toEmail, String pnr) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Booking Confirmed - PNR: " + pnr);
        message.setText("Your booking is successful.\nPNR: " + pnr);
        mailSender.send(message);
    }

    public void sendCancellationEmail(String toEmail, String pnr) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Booking Cancelled - PNR: " + pnr);
        message.setText("Your booking has been cancelled.\nPNR: " + pnr);
        mailSender.send(message);
    }
}

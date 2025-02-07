package com.example.eventplanning.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class EmailService {
    private final Logger logger = Logger.getLogger(EmailService.class.getName());

    public CompletableFuture<Void> sendConfirmationEmail(String to, String eventName) {
        logger.info("Start of async execution");
        return CompletableFuture.runAsync(() -> {
            try {
                // Mock email sending by logging the details
                logger.info("Sending confirmation email to " + to + " for event: " + eventName);
                // Simulate delay
                Thread.sleep(1000);
                logger.info("Email sent successfully to " + to);
            } catch (InterruptedException e) {
                logger.warning("Failed to send confirmation email to " + to);
                Thread.currentThread().interrupt();
            }
        }).thenRun(() -> logger.info("End of async execution"));
    }
}

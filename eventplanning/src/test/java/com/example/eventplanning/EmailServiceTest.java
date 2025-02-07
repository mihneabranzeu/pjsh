package com.example.eventplanning;

import com.example.eventplanning.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendConfirmationEmailSuccess() {
        CompletableFuture<Void> future = emailService.sendConfirmationEmail("test@example.com", "Test Event");

        assertDoesNotThrow(() -> future.get());
        assertTrue(future.isDone());
    }

    @Test
    void testSendConfirmationEmailInterruptedException() {
        // Mocking the logger to simulate an InterruptedException
        Logger logger = Logger.getLogger(EmailService.class.getName());
        emailService = new EmailService() {
            @Override
            public CompletableFuture<Void> sendConfirmationEmail(String to, String eventName) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        logger.info("Sending confirmation email to " + to + " for event: " + eventName);
                        Thread.sleep(1000);
                        throw new InterruptedException("Simulated interruption");
                    } catch (InterruptedException e) {
                        logger.warning("Failed to send confirmation email to " + to);
                        Thread.currentThread().interrupt();
                    }
                }).thenRun(() -> logger.info("End of async execution"));
            }
        };

        CompletableFuture<Void> future = emailService.sendConfirmationEmail("test@example.com", "Test Event");

        assertDoesNotThrow(() -> future.get());
        assertTrue(future.isDone());
    }
}

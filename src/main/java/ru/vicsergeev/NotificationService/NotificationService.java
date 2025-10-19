package ru.vicsergeev.NotificationService;

import org.springframework.stereotype.Service;
import ru.vicsergeev.NotificationService.dto.UserEventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Victor 16.10.2025
 */
@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEventDTO event) {
        log.info("=== START EVENT ===");
        log.info("Received event from UserService: operation={}, email={}, name={}", event.operation(), event.email(), event.name());
        log.info("Data received successfully via Kafka, sending email");
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.email());
            message.setSubject("User Event: " + event.operation());
            message.setText("User event: " + event.operation() + " for email: " + event.email() + ", name: " + event.name());
            mailSender.send(message);
            log.info("Email sent to {}", event.email());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", event.email(), e.getMessage());
        }
        log.info("=== END EVENT ===");
    }

    @KafkaListener(topics = "user-events.DLT", groupId = "notification-group-dlt")
    public void handleDltMessage(byte[] message) {
        log.error("Failed to deserialize message, sent to DLT: {}", new String(message));
    }
}
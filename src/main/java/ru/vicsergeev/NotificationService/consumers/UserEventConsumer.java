package ru.vicsergeev.NotificationService.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.vicsergeev.NotificationService.dto.UserEventDTO;
import ru.vicsergeev.NotificationService.service.EmailService;

/**
 * Created by Victor 16.10.2025
 */

@Component
public class UserEventConsumer {
    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEventDTO event) {
        emailService.sendUserEventEmail(event);
    }
}

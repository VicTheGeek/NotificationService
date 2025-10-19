package ru.vicsergeev.NotificationService.service;

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
        log.info("получение данных из UserService: operation={}, email={}, name={}", event.operation(), event.email(), event.name());
        log.info("данные получены, отправляю сообщение");
        try {
            String subject;
            String text;
            if (UserEventDTO.CREATE.equals(event.operation())) {
                subject = "account created";
                text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
            } else if (UserEventDTO.DELETE.equals(event.operation())) {
                subject = "account deleted";
                text = "Здравствуйте! Ваш аккаунт был удалён.";
            } else {
                throw new IllegalArgumentException("unknown operation: " + event.operation());
            }
            sendEmail(event.email(), subject, text);
            log.info("письмо отправлено на {}", event.email());
        } catch (Exception e) {
            log.error("не удалось отправить письмо {}: {}", event.email(), e.getMessage());
        }
        log.info("=== END EVENT ===");
    }

    @KafkaListener(topics = "user-events.DLT", groupId = "notification-group-dlt")
    public void handleDltMessage(byte[] message) {
        log.error("Failed to deserialize message, sent to DLT: {}", new String(message));
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom("aa@aa.aa");
        mailSender.send(msg);
    }
}
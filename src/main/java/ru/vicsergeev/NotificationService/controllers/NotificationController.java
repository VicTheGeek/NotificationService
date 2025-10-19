package ru.vicsergeev.NotificationService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vicsergeev.NotificationService.dto.EmailDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Victor 16.10.2025
 */

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final JavaMailSender mailSender;

    public NotificationController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
        log.info("Sending email to {}", emailDTO.email());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDTO.email());
        message.setSubject(emailDTO.subject());
        message.setText(emailDTO.text());
        mailSender.send(message);
        log.info("Email sent to {}", emailDTO.email());
        return ResponseEntity.ok("Email sent to " + emailDTO.email());
    }
}
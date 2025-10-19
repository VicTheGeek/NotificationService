package ru.vicsergeev.NotificationService.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.vicsergeev.NotificationService.dto.UserEventDTO;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail( String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom("aa@aa.aa");
        mailSender.send(msg);
    }

    public void sendUserEventEmail(UserEventDTO event) {
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
    }
}

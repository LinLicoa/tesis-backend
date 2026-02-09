package com.application.webapi.service.impl;

import com.application.webapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @org.springframework.beans.factory.annotation.Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String content, boolean isHtml) {
        try {
            jakarta.mail.internet.MimeMessage message = javaMailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(
                    message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);

            javaMailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}

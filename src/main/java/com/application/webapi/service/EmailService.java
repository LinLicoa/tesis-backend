package com.application.webapi.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content, boolean isHtml);
}

package com.example.forums_backend.service;

import com.example.forums_backend.entity.EmailDetails;

public interface EmailService {
    // Method
    // To send a simple email
    void sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}

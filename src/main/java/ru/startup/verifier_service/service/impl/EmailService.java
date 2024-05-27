package ru.startup.verifier_service.service.impl;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final Gmail service;
    private static final String EMAIL_MY = "nestorteam925@gmail.com";

    public void sendOtpEmail(String to, String otp) {
        String sender = "From: " + EMAIL_MY + "\r\n";
        String recipient = "To: " + to + "\r\n";
        String subject = "Subject: " + "OTP Code for verifier-service" + "\r\n";
        String body = "\r\n" + "Your OTP code is: "+otp;

        String emailContent = sender + recipient + subject + body;
        Message message = new Message().setRaw(Base64.encodeBase64URLSafeString(emailContent.getBytes()));
        try {
            service.users().messages().send(EMAIL_MY, message).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

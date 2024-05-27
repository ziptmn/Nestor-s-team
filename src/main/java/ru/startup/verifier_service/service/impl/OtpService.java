package ru.startup.verifier_service.service.impl;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final String CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateOtp(String username) {
        String otp = generateOtpCode();
        otpStorage.put(username, otp);
        return otp;
    }

    public boolean validateOtp(String username, String otp) {
        String storedOtp = otpStorage.get(username);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(username);
            return true;
        }
        return false;
    }

    private String generateOtpCode() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return otp.toString();
    }
}


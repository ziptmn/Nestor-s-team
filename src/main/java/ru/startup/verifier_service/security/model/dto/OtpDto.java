package ru.startup.verifier_service.security.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OtpDto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String otp;
}

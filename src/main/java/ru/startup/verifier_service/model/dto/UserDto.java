package ru.startup.verifier_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data transfer object (DTO) representing a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID uuid;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String fullName;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String company;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String inn;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String ogrn;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String position;

    private String role;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String email;
}

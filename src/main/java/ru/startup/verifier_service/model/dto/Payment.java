package ru.startup.verifier_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Payment {

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String bank;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String bik;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String accountNumber;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String correspondentAccountOfTheBank;
}

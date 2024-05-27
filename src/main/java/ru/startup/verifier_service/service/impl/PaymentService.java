package ru.startup.verifier_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.startup.verifier_service.model.User;
import ru.startup.verifier_service.model.dto.Payment;
import ru.startup.verifier_service.security.exception.InvalidInputException;
import ru.startup.verifier_service.security.model.Role;
import ru.startup.verifier_service.security.model.dto.ChangeUserRightsDto;
import ru.startup.verifier_service.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private LocalDateTime mockTransaction;
    private UserService userService;

    public PaymentService(UserService userService) {
        this.userService = userService;
        mockTransaction = LocalDateTime.now();
    }

    public void payment(Payment payment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        mockTransaction = LocalDateTime.now();
        Optional<User> byEmail = userService.findByEmail(authentication.getName());
        if (byEmail.isPresent()
            && (byEmail.get().getRole().equals("USER"))) {
            ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.EXPECTATION);
            userService.changeUserPermissions(byEmail.get().getUuid().toString(), changeUserRightsDto);
        }
    }

    public void transactional(String email) {
        Duration duration = Duration.between(mockTransaction, LocalDateTime.now());
        long hoursDifference = duration.toHours();
        Optional<User> byEmail = userService.findByEmail(email);
        if ((hoursDifference < 2)
            && (byEmail.isPresent())
            && (byEmail.get().getRole().equals("EXPECTATION"))) {
            ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.ADMIN);
            userService.changeUserPermissions(byEmail.get().getUuid().toString(), changeUserRightsDto);
        } else {
            throw new InvalidInputException("Время ожидания транзакции истекло!");
        }
    }
}

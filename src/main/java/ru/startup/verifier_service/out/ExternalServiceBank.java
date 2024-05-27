package ru.startup.verifier_service.out;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.startup.verifier_service.security.model.dto.SuccessResponse;
import ru.startup.verifier_service.service.impl.PaymentService;

@RestController
@RequestMapping("/bank/transaction")
@RequiredArgsConstructor
public class ExternalServiceBank {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> transactional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        paymentService.transactional(authentication.getName());
        return ResponseEntity.ok().body(new SuccessResponse("The transaction has been successfully confirmed"));
    }

}

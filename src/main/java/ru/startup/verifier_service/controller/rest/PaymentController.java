package ru.startup.verifier_service.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.startup.verifier_service.model.CustomFieldError;
import ru.startup.verifier_service.model.dto.Payment;
import ru.startup.verifier_service.security.model.dto.SuccessResponse;
import ru.startup.verifier_service.service.impl.PaymentService;
import ru.startup.verifier_service.util.GeneratorResponseMessage;

import java.util.List;

@RestController
@RequestMapping("/verifier-service/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final GeneratorResponseMessage generatorResponseMessage;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> payment(@RequestBody @Valid Payment payment,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        paymentService.payment(payment);
        return ResponseEntity.ok().body(new SuccessResponse("The transaction is pending"));
    }
}

package ru.startup.verifier_service.controller.UiController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/verifier-service")
public class PaymentControllerUI {
    @GetMapping("/payment")
    public String profileUi() {
        return "personal_account/payment";
    }
}

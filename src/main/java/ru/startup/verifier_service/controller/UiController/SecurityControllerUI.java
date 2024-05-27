package ru.startup.verifier_service.controller.UiController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/verifier-service/auth")
public class SecurityControllerUI {

    @GetMapping
    public String showForms() {
        return "auth/main";
    }


    @GetMapping("/otp-page")
    public String showFormsOTP() {
        return "auth/otp-page";
    }
}

package ru.startup.verifier_service.controller.UiController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/verifier-service")
public class UserControllerUI {

    @GetMapping("/profile-ui")
    public String profileUi() {
        return "personal_account/profile";
    }
}

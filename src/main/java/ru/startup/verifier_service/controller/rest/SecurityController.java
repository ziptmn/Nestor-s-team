package ru.startup.verifier_service.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.startup.verifier_service.mapper.UserMapper;
import ru.startup.verifier_service.model.CustomFieldError;
import ru.startup.verifier_service.model.User;
import ru.startup.verifier_service.security.model.dto.*;
import ru.startup.verifier_service.security.model.JwtResponse;
import ru.startup.verifier_service.service.UserService;
import ru.startup.verifier_service.service.impl.EmailService;
import ru.startup.verifier_service.service.impl.OtpService;
import ru.startup.verifier_service.util.GeneratorResponseMessage;

import java.util.List;

/**
 * Controller class for handling authentication operations.
 */
@RestController
@RequestMapping("/verifier-service/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final UserMapper userMapper;
    private final OtpService otpService;
    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationDto registrationDto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        String otp = otpService.generateOtp(registrationDto.getEmail());
        emailService.sendOtpEmail(registrationDto.getEmail(), otp);
        return ResponseEntity.ok(new SuccessResponse("OTP sent to your email"));
    }

    @PostMapping("/sign-up/otp")
    public ResponseEntity<?> otpPostSignUp(@RequestParam(value = "code") String code,
                                           @RequestBody @Valid RegistrationDto registrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        if (otpService.validateOtp(registrationDto.getEmail(), code)) {
            User user = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
        } else {
            return ResponseEntity.badRequest().body(new ExceptionResponse("Invalid OTP"));
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authorize(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        boolean correct = userService.verifierPassword(loginDto);
        if(correct){
            String otp = otpService.generateOtp(loginDto.getEmail());
            emailService.sendOtpEmail(loginDto.getEmail(), otp);
            return ResponseEntity.ok(new SuccessResponse("OTP send to your email"));
        }
        return ResponseEntity.badRequest().body(new ExceptionResponse("The password for this email is incorrect."));
    }

    @PostMapping("/sign-in/otp")
    public ResponseEntity<?> otpPostSignIn(@RequestBody @Valid OtpDto otpDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        if (otpService.validateOtp(otpDto.getEmail(), otpDto.getOtp())) {
            JwtResponse response = userService.login(otpDto.getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ExceptionResponse("Invalid OTP"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> extendTheLifeOfTheToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        JwtResponse response = userService.updateToken(refreshTokenDto.refreshToken().trim());
        return ResponseEntity.ok(response);
    }
}
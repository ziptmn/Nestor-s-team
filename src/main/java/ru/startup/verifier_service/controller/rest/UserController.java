package ru.startup.verifier_service.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.startup.verifier_service.mapper.UserMapper;
import ru.startup.verifier_service.model.CustomFieldError;
import ru.startup.verifier_service.model.User;
import ru.startup.verifier_service.security.model.dto.SuccessResponse;
import ru.startup.verifier_service.security.model.dto.ChangeUserRightsDto;
import ru.startup.verifier_service.service.UserService;
import ru.startup.verifier_service.util.GeneratorResponseMessage;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/verifier-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok(userMapper.toDto(user.get()));
    }

    @PatchMapping("/users/{uuid}/access")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeUserRights(@PathVariable("uuid") String uuid,
                                              @RequestBody @Valid ChangeUserRightsDto changeUserRightsDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        userService.changeUserPermissions(uuid, changeUserRightsDto);
        return ResponseEntity.ok(new SuccessResponse("Права доступа успешно изменена!"));
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(new SuccessResponse("Учетная запись удалена"));
    }
}

package ru.startup.verifier_service.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import ru.startup.verifier_service.model.CustomFieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class GeneratorResponseMessage {

    public List<CustomFieldError> generateErrorMessage(BindingResult bindingResult) {
        List<CustomFieldError> errors = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                CustomFieldError customFieldError = new CustomFieldError(
                        fieldError.getDefaultMessage(),
                        fieldError.getField()
                );
                errors.add(customFieldError);
            }
        }
        return errors;
    }
}

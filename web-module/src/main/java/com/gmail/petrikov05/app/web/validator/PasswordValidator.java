package com.gmail.petrikov05.app.web.validator;

import com.gmail.petrikov05.app.service.model.user.PasswordDTO;
import com.gmail.petrikov05.app.web.constant.MessageConstant;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_CHANGE_DIFFERENT_PASSWORD_FAIL;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordDTO password = (PasswordDTO) o;
        if (!password.getNewPassword().equals(password.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "", MESSAGE_CHANGE_DIFFERENT_PASSWORD_FAIL);
        }
    }

}

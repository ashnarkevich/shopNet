package com.gmail.petrikov05.app.web.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice(basePackages = "com.gmail.petrikov05.app.web.controller.api")
public class APIExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ResponseError handleBadRequest(MethodArgumentNotValidException e) {
        ResponseError responseError = new ResponseError();
        List<ObjectError> bindingResErrors = e.getBindingResult().getAllErrors();
        List<String> errors = bindingResErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        responseError.setErrors(errors);
        return responseError;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AnonymousUserException.class)
    @ResponseBody
    String handleBadRequest(AnonymousUserException e) {
        return e.getMessage();
    }

    public static class ResponseError {

        private List<String> errors;

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public List<String> getErrors() {
            return errors;
        }

    }

}

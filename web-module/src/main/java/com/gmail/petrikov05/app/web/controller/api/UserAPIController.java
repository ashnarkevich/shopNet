package com.gmail.petrikov05.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserAPIController {

    private final UserService userService;

    public UserAPIController(UserService userService) {this.userService = userService;}

    @PostMapping
    public Object addUser(
            @RequestBody @Valid AddUserDTO addUserDTO
    ) {
        try {
            UserDTO addedUser = userService.addUser(addUserDTO);
            return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
        } catch (UserExistenceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

}

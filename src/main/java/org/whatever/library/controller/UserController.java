package org.whatever.library.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.security.SecurityService;
import org.whatever.library.security.User;
import org.whatever.library.services.UserService;
import org.whatever.library.validation.UserValidator;

@RestController
public class UserController {

    private UserService userService;
    private UserValidator userValidator;
    private SecurityService securityService;

    public UserController(UserService userService, UserValidator userValidator, SecurityService securityService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.securityService = securityService;
    }

    @CrossOrigin
    @GetMapping("/users")
    public User findByUsername(@RequestBody String username) {
        return userService.findByUsername(username);
    }

    @CrossOrigin
    @PostMapping("/registration")
    public User registration(@RequestBody User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return userForm;
        }

        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return userForm;
    }
}

package org.whatever.library.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.security.SecurityService;
import org.whatever.library.models.User;
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
    @ResponseBody
    @PostMapping("/temp/login")
    public boolean login(@RequestBody User user) {
        try {
            if (user.getPassword().equals(user.getPasswordConfirm())) {
                securityService.autoLogin(user.getUsername(), user.getPasswordConfirm());
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @CrossOrigin
    @GetMapping("/users")
    public User findByUsername(@RequestBody String username) {
        return userService.findByUsername(username);
    }

    @CrossOrigin
    @GetMapping("/user")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @CrossOrigin
    @PostMapping("/registration/mod")
    public User registerModerator(@RequestBody User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return userForm;
        }

        userService.register(userService.createModerator(userForm));
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return userForm;
    }

    @CrossOrigin
    @PostMapping("/registration")
    public User registration(@RequestBody User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return userForm;
        }

        userService.register(userService.createUser(userForm));
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return userForm;
    }
}

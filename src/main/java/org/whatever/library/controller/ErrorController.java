package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.whatever.library.validation.ValidationErrorPasser;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @Autowired
    private ValidationErrorPasser errorPasser;

    @RequestMapping("error")
    public @ResponseBody
    String
    handleError() {
        String messages = errorPasser.getMessagesAsString();
        errorPasser.markAllAsRead();
        return messages;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

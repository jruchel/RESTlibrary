package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.whatever.library.validation.ValidationErrorPasser;

import java.util.List;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @Autowired
    private ValidationErrorPasser errorPasser;

    //ALL
    @RequestMapping("/error")
    public @ResponseBody
    List<String>
    handleError() {
        return errorPasser.getMessagesAsList();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

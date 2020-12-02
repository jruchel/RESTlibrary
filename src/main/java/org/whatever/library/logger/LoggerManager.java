package org.whatever.library.logger;

import java.util.ArrayList;
import java.util.List;

public class LoggerManager {

    private List<KeyLogger> keyLoggerList;

    public LoggerManager() {
        keyLoggerList = new ArrayList<>();
    }

    public void consumeMessage(String message, String id) {
        getLoggerWithID(id).processMessage(message);
    }

    private KeyLogger getLoggerWithID(String id) {
        KeyLogger keyLogger = keyLoggerList.stream().filter(kl -> kl.getId().equals(id)).findFirst().orElse(null);
        if (keyLogger != null)
            return keyLogger;
        else {
            keyLoggerList.add(new KeyLogger(id));
            return getLoggerWithID(id);
        }
    }

}

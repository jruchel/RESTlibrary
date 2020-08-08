package org.whatever.library.security;

public class IllegalActionException extends Exception {

    public IllegalActionException(String msg) {
        super(msg);
    }

    public IllegalActionException() {
        this("Illegal action");
    }
}

package org.whatever.library.services;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
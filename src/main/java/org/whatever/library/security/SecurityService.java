package org.whatever.library.security;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
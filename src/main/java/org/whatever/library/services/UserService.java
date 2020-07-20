package org.whatever.library.services;

import org.whatever.library.security.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
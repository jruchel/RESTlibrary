package org.whatever.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.whatever.library.security.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}

package org.whatever.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.whatever.library.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}

package org.whatever.library.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.whatever.library.models.User;
import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "SELECT user_id FROM users_books_renting", nativeQuery = true)
    Set<Integer> getRentingUsers();

    @Query(value = "SELECT user_id FROM users_books_reserving", nativeQuery = true)
    Set<Integer> getReservingUsers();
}

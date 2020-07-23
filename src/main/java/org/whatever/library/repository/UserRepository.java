package org.whatever.library.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.whatever.library.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);


    @Query(value = "SELECT user_id FROM users_books_renting", nativeQuery = true)
    List<Integer> getRentingUsers();

    @Query(value = "SELECT user_id FROM users_books_reserving", nativeQuery = true)
    List<Integer> getReservingUsers();
}

package org.whatever.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whatever.library.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select * from library.role where name = ?1", nativeQuery = true)
    Role getRoleByName(String name);

}

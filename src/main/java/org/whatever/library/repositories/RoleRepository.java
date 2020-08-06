package org.whatever.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whatever.library.models.Role;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select * from library.role where name = ?1", nativeQuery = true)
    Role getRoleByName(String name);

    @Query(value ="select * from library.role", nativeQuery = true)
    Set<Role> getAllRoles();

}

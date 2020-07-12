package org.whatever.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whatever.library.security.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

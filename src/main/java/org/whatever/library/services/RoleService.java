package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.repository.RoleRepository;
import org.whatever.library.security.Role;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    private Set<Role> createBasicRoles() {
        Set<Role> roles = new HashSet<>();
        Role admin = new Role();
        admin.setName("ADMIN");
        Role user = new Role();
        user.setName("USER");
        Role mod = new Role();
        mod.setName("MODERATOR");
        roles.add(admin);
        roles.add(user);
        roles.add(mod);
        return roles;
    }

    @PostConstruct
    public void initRoles() {
        for (Role r : createBasicRoles()) {
            roleRepository.save(r);
        }
    }

}

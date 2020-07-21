package org.whatever.library.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.whatever.library.repository.RoleRepository;
import org.whatever.library.repository.UserRepository;
import org.whatever.library.security.Role;
import org.whatever.library.security.User;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    private void createAdmin() {
        if (userRepository.findByUsername("admin") != null) return;
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin1");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        for (Role r : roleRepository.getAllRoles()) {
            user.giveRole(r);
        }
        userRepository.save(user);
    }

    private Set<Role> createBasicRoles() {
        Set<Role> roles = new HashSet<>();
        Role admin = new Role();
        admin.setName("ROLE_ADMIN");
        Role user = new Role();
        user.setName("ROLE_USER");
        Role mod = new Role();
        mod.setName("ROLE_MODERATOR");
        roles.add(admin);
        roles.add(user);
        roles.add(mod);
        return roles;
    }

    private void initializeRoles() {
        for (Role r : createBasicRoles()) {
            if (roleRepository.getRoleByName(r.getName()) == null)
                roleRepository.save(r);
        }
    }

    @PostConstruct
    private void initialize() {
        initializeRoles();
        createAdmin();
    }

}

package org.whatever.library.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.whatever.library.repositories.RoleRepository;
import org.whatever.library.repositories.UserRepository;
import org.whatever.library.models.Role;
import org.whatever.library.models.User;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    private void createAdmin() {
        if (userRepository.findByUsername("admin") != null) return;
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin1");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        for (Role r : roleRepository.getAllRoles()) {
            user.giveRole(r);
        }
        userRepository.save(user);
    }

    private void createUser(String username, String password) {
        if (userRepository.findByUsername(username) != null) return;
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.giveRole(roleRepository.getRoleByName("ROLE_USER"));
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
        Role sub = new Role();
        sub.setName("ROLE_SUBSCRIBER");
        roles.add(admin);
        roles.add(user);
        roles.add(mod);
        roles.add(sub);
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
        createUser("kuba", "admin1");
        createUser("Krystian", "jakubruchel123");
    }

}

package org.whatever.library.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.whatever.library.repository.RoleRepository;
import org.whatever.library.repository.UserRepository;
import org.whatever.library.services.UserService;

import javax.annotation.PostConstruct;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.giveRole(roleRepository.getRoleByName("USER"));
        userRepository.save(user);
    }

    @DependsOn("RoleService")
    @PostConstruct
    private void createAdmin() {
        if (userRepository.findByUsername("admin") != null) return;
        User user = new User();
        user.setUsername("admin");
        user.setPassword("hasloAdmina");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.giveRole(roleRepository.getRoleByName("ADMIN"));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
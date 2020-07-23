package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.whatever.library.model.User;
import org.whatever.library.repository.RoleRepository;
import org.whatever.library.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getRentingUsers() {
        List<Integer> rentingIds = userRepository.getRentingUsers();
        List<User> rentingUsers = new ArrayList<>();
        for (Integer id : rentingIds) {
            userRepository.findById(Long.valueOf(id)).ifPresent(rentingUsers::add);
        }
        return rentingUsers;
    }

    public List<User> getReservingUsers() {
        List<Integer> rentingIds = userRepository.getReservingUsers();
        List<User> reservingUsers = new ArrayList<>();
        for (Integer id : rentingIds) {
            userRepository.findById(Long.valueOf(id)).ifPresent(reservingUsers::add);
        }
        return reservingUsers;
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());
        user.giveRole(roleRepository.getRoleByName("ROLE_USER"));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
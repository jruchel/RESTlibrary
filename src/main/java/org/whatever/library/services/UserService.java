package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.whatever.library.model.User;
import org.whatever.library.repository.RoleRepository;
import org.whatever.library.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getRentingUsers() {
        Set<Integer> rentingIds = userRepository.getRentingUsers();
        List<User> rentingUsers = new ArrayList<>();
        for (Integer id : rentingIds) {
            rentingUsers.add(findByID(id));
        }
        return rentingUsers;
    }

    public List<User> getReservingUsers() {
        Set<Integer> rentingIds = userRepository.getReservingUsers();
        List<User> reservingUsers = new ArrayList<>();
        for (Integer id : rentingIds) {
            reservingUsers.add(findByID(id));
        }
        return reservingUsers;
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());
        user.giveRole(roleRepository.getRoleByName("ROLE_USER"));
        userRepository.save(user);
    }

    public User findByID(int id) {
        if (userRepository.findById((long) id).isPresent()) {
            return userRepository.findById((long) id).get();
        }
        return null;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
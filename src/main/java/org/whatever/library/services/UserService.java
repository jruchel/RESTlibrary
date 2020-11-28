package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.whatever.library.models.User;
import org.whatever.library.repositories.RefundRepository;
import org.whatever.library.repositories.TransactionRepository;
import org.whatever.library.repositories.UserRepository;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RefundRepository refundRepository;
    @Autowired
    private RoleService roleService;
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

    public User createAdmin(String username, String password) {
        User user = new User();
        user.setPassword(password);
        user.setPasswordConfirm(password);
        user.setUsername(username);
        user.setRoles(roleService.getAdminRoles());

        return user;
    }

    public User createUser(String username, String password) {
        User user = new User();
        user.setPassword(password);
        user.setPasswordConfirm(password);
        user.setUsername(username);
        user.setRoles(roleService.getUserRoles());

        return user;
    }

    public User createModerator(String username, String password) {
        User user = new User();
        user.setPassword(password);
        user.setPasswordConfirm(password);
        user.setUsername(username);
        user.setRoles(roleService.getModeratorRoles());

        return user;
    }

    public User getUserWithRefund(int refundID) {
        return userRepository.findById((long) transactionRepository.getUserIDWithTransaction(refundRepository.getTransactionIDWithRefund(refundID))).orElse(null);
    }

    public User createUser(User user) {
        return createUser(user.getUsername(), user.getPasswordConfirm());
    }

    public User createModerator(User user) {
        return createModerator(user.getUsername(), user.getPasswordConfirm());
    }

    public User createAdmin(User user) {
        return createAdmin(user.getUsername(), user.getPasswordConfirm());
    }

    public String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        return principal.toString();
    }

    public User getCurrentUser() {
        return findByUsername(getPrincipalUsername());
    }

    public void register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        save(user);
    }

    public void save(User user) {
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
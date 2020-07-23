package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Admin;
import org.whatever.library.model.Book;
import org.whatever.library.model.User;
import org.whatever.library.repository.UserRepository;
import org.whatever.library.services.BookService;
import org.whatever.library.services.RentalService;
import org.whatever.library.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rental")
public class RentalController {

    @Autowired
    private Admin admin;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;


    @Autowired
    private RentalService rentalService;

    @CrossOrigin
    @PostMapping("/reserve")
    public void reserveBook(@RequestBody int bid) {
        rentalService.reserveBook(getPrincipalUsername(), bid);
    }

    @CrossOrigin
    @DeleteMapping("/reserve")
    public void cancelReservation(@RequestBody int bid) {
        rentalService.cancelReservation(getPrincipalUsername(), bid);
    }

    @CrossOrigin
    @GetMapping("/renting")
    public List<User> rentingUsers() {
        return rentalService.getRentingUsers();
    }

    @CrossOrigin
    @GetMapping("/reserving")
    public List<User> reservingUsers() {
        return rentalService.getReservingUsers();
    }

    @CrossOrigin
    @PostMapping("/renting")
    public void rentBook(@RequestBody Map<String, Integer> params) {
        int userID = params.get("uid");
        int bookID = params.get("bid");

        User user = userService.findByID(userID);
        Book book = bookService.findByID(bookID);

        admin.rentBook(user, book);
    }


    // ===== private methods =================
    private String getPrincipalUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return ((UserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
        return principal.toString();
    }


}

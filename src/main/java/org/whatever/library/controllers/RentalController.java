package org.whatever.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.models.Admin;
import org.whatever.library.models.Book;
import org.whatever.library.models.User;
import org.whatever.library.services.BookService;
import org.whatever.library.services.PaymentService;
import org.whatever.library.services.RentalService;
import org.whatever.library.services.UserService;

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
    private PaymentService paymentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private RentalService rentalService;

    @CrossOrigin
    @PostMapping("/reserve/{id}")
    public boolean reserveBook(@PathVariable int id) {
        return rentalService.reserveBook(userService.getPrincipalUsername(), id);
    }

    @CrossOrigin
    @DeleteMapping("/reserve/{id}")
    public void cancelReservation(@PathVariable int id) {
        rentalService.cancelReservation(userService.getPrincipalUsername(), id);
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

    @CrossOrigin
    @PostMapping("/return")
    public void returnBook(@RequestBody int bid) {
        rentalService.returnBook(userService.getPrincipalUsername(), bid);
    }

}

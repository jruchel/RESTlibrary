package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.models.Book;
import org.whatever.library.models.User;
import org.whatever.library.repositories.AuthorRepository;
import org.whatever.library.repositories.BookRepository;
import org.whatever.library.repositories.UserRepository;

import java.util.List;

@Service
public class RentalService {

    @Autowired
    private AuthorRepository autorRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean reserveBook(String username, int bid) {
        if (canReserve(bid, 1)) {
            User user = userRepository.findByUsername(username);
            if (!user.hasRole("ROLE_SUBSCRIBER")) return false;
            Book book = getBook(bid);
            if (book != null) {
                user.reserveBook(book);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public void returnBook(String username, int bid) {
        User user = userRepository.findByUsername(username);
        Book book = getBook(bid);
        if (book != null) {
            book.returnBook(user);
            user.returnBook(book);
            userRepository.save(user);
        }

    }

    public void cancelReservation(String username, int bid) {
        User user = userRepository.findByUsername(username);
        Book book = getBook(bid);
        if (book != null) {
            book.cancelReservation(user);
            user.cancelReservation(bid);
            userRepository.save(user);
            bookRepository.save(book);
        }

    }

    public List<User> getRentingUsers() {
        return userService.getRentingUsers();
    }

    public List<User> getReservingUsers() {
        return userService.getReservingUsers();
    }

    private Book getBook(int bid) {
        if (bookRepository.findById(bid).isPresent())
            return bookRepository.findById(bid).get();

        return null;
    }

    private boolean canReserve(int bid, int amount) {
        Book book = getBook(bid);
        if (book == null) return false;
        return book.getInStock() >= amount;
    }
}

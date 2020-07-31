package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.model.User;
import org.whatever.library.repository.AuthorRepository;
import org.whatever.library.repository.BookRepository;
import org.whatever.library.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

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

    public void reserveBook(String username, int bid) {
        if (canReserve(bid, 1)) {
            User user = userRepository.findByUsername(username);
            Book book = getBook(bid);
            if (book != null) {
                user.reserveBook(book);
                userRepository.save(user);
            }
        }
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

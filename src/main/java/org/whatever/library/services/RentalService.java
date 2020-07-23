package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.model.User;
import org.whatever.library.repository.AuthorRepository;
import org.whatever.library.repository.BookRepository;
import org.whatever.library.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
public class RentalService {

    @Autowired
    private AuthorRepository autorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public void reserveBook(String username, int bid) {
        if (canReserve(bid, 1)) {
            User user = userRepository.findByUsername(username);
            Book book = getBook(bid);
            user.reserveBook(book);
            userRepository.save(user);
        }
    }

    public void cancelReservation(String username, int bid) {
        User user = userRepository.findByUsername(username);
        Book book = getBook(bid);
        book.cancelReservation(user);
        user.cancelReservation(bid);
        userRepository.save(user);
        bookRepository.save(book);
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

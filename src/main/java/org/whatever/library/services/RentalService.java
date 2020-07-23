package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.model.User;
import org.whatever.library.repository.AuthorRepository;
import org.whatever.library.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
public class RentalService {

    @Autowired
    private AuthorRepository autorRepository;

    @Autowired
    private UserRepository userRepository;

    public void reserveBook(String username, int aid, int bid) {
        if (canReserve(aid, bid, 1)) {
            User user = userRepository.findByUsername(username);
            user.reserveBook(getBook(aid, bid));
            userRepository.save(user);
        }
    }

    private Book getBook(int aid, int bid) {
        try {
            Author author = autorRepository.findById(aid).get();
            return author.getBook(bid);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    private boolean canReserve(int aid, int bid, int amount) {
        Book book = getBook(aid, bid);
        if (book == null) return false;
        return book.getInStock() >= amount;
    }
}

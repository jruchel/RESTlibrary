package org.whatever.library.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whatever.library.services.UserService;

@Component
public class Admin {

    @Autowired
    private UserService userService;

    public void rentBook(User user, Book book) {
        if (user.getReservedBooks().contains(book)) {
            user.getReservedBooks().remove(book);
            user.getRentedBooks().add(book);
            book.getReservingUsers().remove(user);
            book.getRentingUsers().add(user);
            userService.save(user);
        }
    }

}

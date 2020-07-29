package org.whatever.library.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.model.User;
import org.whatever.library.utils.ParsingUtils;

import java.io.IOException;

@RestController
@RequestMapping("admin/utils")
public class UtilityController {


    @CrossOrigin
    @GetMapping("/json/user")
    public String getUserJSONPattern() {
        User user = new User();
        user.setUsername("{}");
        user.setPassword("{}");
        user.setPasswordConfirm("{}");

        try {
            return ParsingUtils.objectToJSON(user, "reservedBooks", "rentedBooks", "roles", "id");
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping("/json/author")
    public String getAuthorJSONPattern() {
        Author author = new Author();
        author.setName("{}");
        Book book = new Book();
        book.setTitle("{}");
        author.addBook(book);
        try {
            return ParsingUtils.objectToJSON(author, "rentingCount", "reservedCount");
        } catch (IOException e) {
            return e.getMessage();
        }
    }


}

//TODO remove ignored properties from json
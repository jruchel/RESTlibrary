package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.whatever.library.model.Author;
import org.whatever.library.services.AuthorService;


@RestController()
@RequestMapping()
public class AuthorsController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public @ResponseBody
    Iterable<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

}

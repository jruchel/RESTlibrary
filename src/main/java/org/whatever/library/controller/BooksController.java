package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Book;
import org.whatever.library.services.BookService;



@RestController
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public @ResponseBody
    Iterable<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/book/{id}")
    public @ResponseBody
    Book getBookByID(@PathVariable int id) {
        return bookService.getBookByID(id);
    }

    @PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBook(@RequestBody Book book) {

        if (bookService.exists(book)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        bookService.save(book);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


}

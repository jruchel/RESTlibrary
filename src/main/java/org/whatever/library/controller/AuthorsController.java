package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.LibraryService;


@RestController()
public class AuthorsController {

    @Autowired
    private LibraryService libraryService;

    @GetMapping("/authors")
    public @ResponseBody
    Iterable<Author> getAllAuthors() {
        return libraryService.getAllAuthors();
    }

    @GetMapping("/authors/{id}")
    public @ResponseBody
    Author getAuthorByID(@PathVariable int id) {
        return libraryService.getAuthorByID(id);
    }

    @PostMapping(value = "/authors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAuthor(@RequestBody Author author) {
        if (libraryService.exists(author)) return new ResponseEntity(HttpStatus.CONFLICT);
        libraryService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping("/authors/{id}/books")
    public @ResponseBody
    Iterable<Book> getAllBooks(@PathVariable int id) {
        return getAuthorByID(id).getBibliography();
    }

    @GetMapping("/authors/{id}/{bid}")
    public @ResponseBody
    Book getBookByID(@PathVariable int id, @PathVariable int bid) {
        return getAuthorByID(id).getBook(bid);
    }

    @PostMapping(value = "/authors/{id}/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBook(@PathVariable int id, @RequestBody Book book) {
        Author author = getAuthorByID(id);
        author.addBook(book);
        libraryService.save(author);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}

package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.LibraryService;


@RestController()
public class LibraryController {

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

    @PostMapping(value = "/authors")
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

    @PostMapping(value = "/authors/{id}/book")
    public ResponseEntity addBook(@PathVariable int id, @RequestBody Book book) {
        Author author = getAuthorByID(id);
        author.addBook(book);
        libraryService.save(author);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/authors/{id}/{bid}")
    public ResponseEntity deleteBook(@PathVariable int id, @PathVariable int bid) {
        if (libraryService.getBookByID(id, bid) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Author author = getAuthorByID(id);
        author.removeBook(bid);
        libraryService.deleteBookByID(bid);
        libraryService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable int id) {
        if (libraryService.getAuthorByID(id) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        libraryService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}

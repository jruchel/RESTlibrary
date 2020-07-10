package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.LibraryService;

import java.util.List;


@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @CrossOrigin
    @GetMapping("/authors")
    public @ResponseBody
    Iterable<Author> getAllAuthors() {
        return libraryService.getAllAuthors();
    }

    @CrossOrigin
    @GetMapping("/authors/{id}")
    public @ResponseBody
    Author getAuthorByID(@PathVariable int id) {
        return libraryService.getAuthorByID(id);
    }

    @CrossOrigin
    @PostMapping(value = "/authors")
    public ResponseEntity addAuthor(@RequestBody Author author) {
        if (libraryService.exists(author)) return new ResponseEntity(HttpStatus.CONFLICT);
        libraryService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @PutMapping(value = "/authors/{id}")
    public ResponseEntity updateAuthor(@RequestBody Author author, @PathVariable int id) {
        Author oldAuthor = getAuthorByID(id);

        oldAuthor.setFirstName(author.getFirstName());
        oldAuthor.setLastName(author.getLastName());
        for (Book b : oldAuthor.getBibliography()) {
            deleteBook(id, b.getId());
            if (oldAuthor.getBibliography() == null || oldAuthor.getBibliography().size() == 0) break;
        }

        oldAuthor.setBibliography(author.getBibliography());

        libraryService.save(oldAuthor);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @GetMapping("/authors/{id}/books")
    public @ResponseBody
    Iterable<Book> getAllBooks(@PathVariable int id) {
        return getAuthorByID(id).getBibliography();
    }

    @CrossOrigin
    @GetMapping("/authors/{id}/{bid}")
    public @ResponseBody
    Book getBookByID(@PathVariable int id, @PathVariable int bid) {
        return getAuthorByID(id).getBook(bid);
    }

    @CrossOrigin
    @GetMapping("/authors/search/{name}")
    public @ResponseBody
    List<Author> getAuthorsNamed(@PathVariable String name, @RequestParam(required = false) String lastName) {
        if (lastName == null || lastName.isEmpty()) return libraryService.getAuthorsByName(name);
        return libraryService.getAuthorsNamed(name, lastName);
    }

    @CrossOrigin
    @GetMapping("/authors/search/")
    public @ResponseBody
    List<Author> findAuthorsWithBookTitled(@RequestParam("title") String title) {
        return libraryService.getAuthorsWithBookTitled(title);
    }

    @CrossOrigin
    @PostMapping(value = "/authors/{id}/book")
    public ResponseEntity addBook(@PathVariable int id, @RequestBody Book book) {
        Author author = getAuthorByID(id);
        author.addBook(book);
        libraryService.save(author);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @DeleteMapping(value = "/authors/{id}/{bid}")
    public ResponseEntity deleteBook(@PathVariable int id, @PathVariable int bid) {
        if (libraryService.getBookByID(id, bid) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Author author = getAuthorByID(id);
        author.removeBook(bid);
        libraryService.deleteBookByID(bid);
        libraryService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @DeleteMapping(value = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable int id) {
        if (libraryService.getAuthorByID(id) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        libraryService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}

package org.whatever.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.LibraryService;
import org.whatever.library.utils.Utils;

import java.util.List;

@RestController
public class BookController {

    private LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
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
    @PostMapping(value = "/authors/{id}/book")
    public ResponseEntity addBook(@PathVariable int id, @RequestBody Book book) {
        Author author = getAuthorByID(id);
        author.addBook(book);
        libraryService.save(author);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @PostMapping("/authors/bookDelivery")
    public void addBooks(@RequestBody List<Author> byAuthors) {
        for (Author a : byAuthors) {
            try {
                Author author = libraryService.getAuthorsByName(a.getFirstName(), a.getLastName()).get(0);
                author.addBooks(a.getBibliography());
                author.setBibliography(Utils.compress(author.getBibliography()));
                libraryService.save(author);
            }
            catch (Exception ex) {
                a.setBibliography(Utils.compress(a.getBibliography()));
                libraryService.save(a);
            }
        }
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

    // ==== private methods ====
   private Author getAuthorByID(int id) {
        return libraryService.getAuthorByID(id);
    }

}

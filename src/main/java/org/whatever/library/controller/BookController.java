package org.whatever.library.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.AuthorService;
import org.whatever.library.services.BookService;
import org.whatever.library.utils.Utils;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private AuthorService authorService;
    private BookService bookService;

    public BookController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @CrossOrigin
    @GetMapping("/count")
    public int getBookCount() {
        return bookService.getBookCount();
    }

    @CrossOrigin
    @GetMapping("/size")
    public int getBooksAmount() {
        return bookService.getLibrarySize();
    }

    @CrossOrigin
    @GetMapping("/{id}/books")
    public @ResponseBody
    Iterable<Book> getAllBooks(@PathVariable int id) {
        return getAuthorByID(id).getBibliography();
    }

    @CrossOrigin
    @GetMapping("/{bid}")
    public @ResponseBody
    Book getBookByID(@PathVariable int bid) {
        return bookService.findByID(bid);
    }

    @CrossOrigin
    @PostMapping(value = "/{id}/book")
    public ResponseEntity addBook(@PathVariable int id, @RequestBody Book book) {
        Author author = getAuthorByID(id);
        author.addBook(book);
        authorService.save(author);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @PostMapping("/bookDelivery")
    public void addBooks(@RequestBody List<Author> byAuthors,
                         @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                         @RequestParam(required = false, name = "elements", defaultValue = "25") int elements) {
        for (Author a : byAuthors) {
            try {
                Author author = authorService.getAuthorsByName(a.getName(), page, elements).get(0);
                author.addBooks(a.getBibliography());
                author.setBibliography(Utils.compress(author.getBibliography()));
                authorService.save(author);
            } catch (Exception ex) {
                a.setBibliography(Utils.compress(a.getBibliography()));
                authorService.save(a);
            }
        }
    }

    @CrossOrigin
    @GetMapping("/search")
    public Page<Book> findBooks(@RequestBody String title, @RequestParam(required = false, defaultValue = "10", name = "show") int toShow) {
        return bookService.getBookByTitle(title, toShow);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{bid}")
    public ResponseEntity deleteBook(@PathVariable int bid) {
        if (bookService.findByID(bid) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Book book = bookService.findByID(bid);
        if (book.getRentingCount() + book.getReservedCount() > 0)
            return new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
        Author author = book.getAuthor();
        author.removeBook(bid);
        authorService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    // ==== private methods ====
    private Author getAuthorByID(int id) {
        return authorService.getAuthorByID(id);
    }

}

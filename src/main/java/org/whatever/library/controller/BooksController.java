package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.BookService;

import java.util.List;

@RestController
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public @ResponseBody
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/book/{id}")
    public @ResponseBody
    Book getBookByID(@PathVariable int id) {
        return bookService.getBookByID(id);
    }

   /* @PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addBook(@RequestBody Book book) {
        bookService.save(book);
    }*/

    @GetMapping("/book/test")
    public void addBook() {
        Author author = new Author();
        author.setFirstName("dwaw");
        author.setLastName("dwadaw");

        Book book = new Book();
        book.setTitle("tytul");
        book.setAuthor(author);

        bookService.save(book);
    }

}

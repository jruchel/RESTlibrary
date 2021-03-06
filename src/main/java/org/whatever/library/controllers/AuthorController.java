package org.whatever.library.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.models.Author;
import org.whatever.library.models.Book;
import org.whatever.library.services.AuthorService;
import org.whatever.library.services.BookService;
import org.whatever.library.utils.CollectionUtils;


import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private AuthorService authorService;
    private BookService bookService;
    private CollectionUtils<Author> authorCollectionUtils;

    public AuthorController(AuthorService authorService, CollectionUtils<Author> authorCollectionUtils, BookService bookService) {
        this.authorService = authorService;
        this.authorCollectionUtils = authorCollectionUtils;
        this.bookService = bookService;
    }

    @CrossOrigin
    @GetMapping("/count")
    public int getAuthorsCount() {
        return authorService.getAuthorCount();
    }

    @CrossOrigin
    @GetMapping("/")
    public Iterable<Author> getAllAuthors(@RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                          @RequestParam(required = false, name = "elements", defaultValue = "25") int elements) {
        return authorService.getAllAuthors(page, elements);
    }

    @CrossOrigin
    @GetMapping("/author/book/{bid}")
    public Author getAuthorByBookID(@PathVariable int bid) {
        return authorService.getAuthorByBookID(bid);
    }

    @CrossOrigin
    @GetMapping("/all")
    public Iterable<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    public Author getAuthorByID(@PathVariable() int id) {
        return authorService.getAuthorByID(id);
    }


    @CrossOrigin
    @PostMapping(value = "/")
    public ResponseEntity addAuthor(@RequestBody @Valid Author author) {
        if (authorService.exists(author)) return new ResponseEntity(HttpStatus.CONFLICT);
        for (Book b : author.getBibliography()) {
            if (b.getInStock() <= 0) b.setInStock(1);
        }
        authorService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @CrossOrigin
    @PutMapping(value = "/{id}")
    public ResponseEntity updateAuthor(@RequestBody @Valid Author author, @PathVariable int id) {
        Author oldAuthor = getAuthorByID(id);

        oldAuthor.setName(author.getName());
        for (Book b : oldAuthor.getBibliography()) {
            deleteBook(id, b.getId());
            if (oldAuthor.getBibliography() == null || oldAuthor.getBibliography().size() == 0) break;
        }
        oldAuthor.setBibliography(author.getBibliography());
        authorService.save(oldAuthor);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @CrossOrigin
    @GetMapping("/search")
    public List<Author> searchAuthors(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "lastName") String lastName,
            @RequestParam(required = false, name = "title") String title,
            @RequestParam(required = false, name = "page", defaultValue = "1") int page,
            @RequestParam(required = false, name = "elements", defaultValue = "25") int elements) {
        List<Author> byName = null;
        List<Author> byTitle = null;
        List<Author> byLastName = null;
        if (!(name == null || name.isEmpty())) byName = authorService.getAuthorsByName(name);
        if (!(lastName == null || lastName.isEmpty()))
            byLastName = authorService.getAuthorsByLastName(lastName, page, elements);
        if (!(title == null || title.isEmpty())) byTitle = authorService.getAuthorsWithBookTitled(title);


        return authorCollectionUtils.getCommonObjects(byName, byLastName, byTitle);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteAuthor(@PathVariable() int id) {
        if (authorService.getAuthorByID(id) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        authorService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    // ===== private methods =================
    private ResponseEntity deleteBook(@PathVariable int id, @PathVariable int bid) {
        if (bookService.findByID(bid) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Author author = getAuthorByID(id);
        author.removeBook(bid);
        bookService.deleteBookByID(bid);
        authorService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}

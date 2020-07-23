package org.whatever.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.services.LibraryService;
import org.whatever.library.utils.CollectionUtils;
import org.whatever.library.utils.Utils;


import javax.validation.Valid;
import java.util.List;


@RestController
public class AuthorController {

    private LibraryService libraryService;
    private CollectionUtils<Author> authorCollectionUtils;

    public AuthorController(LibraryService libraryService, CollectionUtils<Author> authorCollectionUtils) {
        this.libraryService = libraryService;
        this.authorCollectionUtils = authorCollectionUtils;
    }

    @CrossOrigin
    @GetMapping("/authors")
    public Iterable<Author> getAllAuthors() {
        return libraryService.getAllAuthors();
    }


    @CrossOrigin
    @GetMapping(value = "/authors/{id}")
    public Author getAuthorByID(@PathVariable() int id) {
        return libraryService.getAuthorByID(id);
    }


    @CrossOrigin
    @PostMapping(value = "/authors")
    public ResponseEntity addAuthor(@RequestBody @Valid Author author) {
        if (libraryService.exists(author)) return new ResponseEntity(HttpStatus.CONFLICT);
        for (Book b : author.getBibliography()) {
            if (b.getInStock() <= 0) b.setInStock(1);
        }
        libraryService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }


    @CrossOrigin
    @PutMapping(value = "/authors/{id}")
    public ResponseEntity updateAuthor(@RequestBody @Valid Author author, @PathVariable int id) {
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
    @GetMapping("/authors/search")
    public List<Author> searchAuthors(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "lastName") String lastName,
            @RequestParam(required = false, name = "title") String title) {
        List<Author> byName = null;
        List<Author> byTitle = null;
        List<Author> byLastName = null;
        if (!(name == null || name.isEmpty())) byName = libraryService.getAuthorsByName(name);
        if (!(lastName == null || lastName.isEmpty())) byLastName = libraryService.getAuthorsByLastName(lastName);
        if (!(title == null || title.isEmpty())) byTitle = libraryService.getAuthorsWithBookTitled(title);


        return authorCollectionUtils.getCommonObjects(byName, byLastName, byTitle);
    }

    @CrossOrigin
    @DeleteMapping(value = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable() int id) {
        if (libraryService.getAuthorByID(id) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        libraryService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    // ===== private methods =================
    private ResponseEntity deleteBook(@PathVariable int id, @PathVariable int bid) {
        if (libraryService.getBookByID(id, bid) == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        Author author = getAuthorByID(id);
        author.removeBook(bid);
        libraryService.deleteBookByID(bid);
        libraryService.save(author);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}

package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.security.User;
import org.whatever.library.services.LibraryService;
import org.whatever.library.security.SecurityService;
import org.whatever.library.services.UserService;
import org.whatever.library.utils.CollectionUtils;
import org.whatever.library.validation.UserValidator;

import javax.validation.Valid;
import java.util.List;


@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;
    @Autowired
    private CollectionUtils<Author> authorCollectionUtils;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

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
    public ResponseEntity addAuthor(@RequestBody @Valid Author author) {
        if (libraryService.exists(author)) return new ResponseEntity(HttpStatus.CONFLICT);
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
    @GetMapping("/authors/search")
    public @ResponseBody
    List<Author> searchAuthors(
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

    @CrossOrigin
    @GetMapping("/users")
    public User findByUsername(@RequestBody String username) {
        return userService.findByUsername(username);
    }

    @CrossOrigin
    @PostMapping("/registration")
    public User registration(@RequestBody User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return userForm;
        }

        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return userForm;
    }

}

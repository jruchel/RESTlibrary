package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.whatever.library.Properties;
import org.whatever.library.models.Author;
import org.whatever.library.models.Book;
import org.whatever.library.repositories.AuthorRepository;
import org.whatever.library.repositories.BookRepository;
import org.whatever.library.utils.CollectionUtils;
import org.whatever.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private CollectionUtils<Author> authorCollectionUtils;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;


    public Iterable<Author> getAllAuthors(int page, int elements) {
        if (elements <= 0) return getAllAuthors(page);
        return authorRepository.findAll(Utils.getPageable(page, elements));
    }

    public Iterable<Author> getAllAuthors(int page) {
        if (page <= 0) return getAllAuthors(1);
        return getAllAuthors(page, 25);
    }

    public Iterable<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorByID(int id) throws NullPointerException {
        if (authorRepository.findById(id).isPresent())
            return authorRepository.findById(id).get();
        else throw new NullPointerException();
    }

    public void save(Author author) {
        authorRepository.save(author);
    }

    public void addBooksToAuthor(Author author, Iterable<Book> books) {
        author.addBooks(books);
        authorRepository.save(author);
    }

    public int getAuthorCount() {
        return authorRepository.getAuthorCount();
    }

    public void addBookToAuthor(Author author, Book book) {
        author.addBook(book);
        authorRepository.save(author);
    }

    public List<Author> getAuthorsByName(String firstName, int page, int elements) {
        return authorRepository.findAuthorsByName(firstName, Utils.getPageable(page, elements));
    }

    public List<Author> getAuthorsByName(String firstName) {
        return authorRepository.findAuthorsByName(firstName);
    }

    public List<Author> getAuthorsByName(String firstName, String lastName, int page, int elements) {
        return authorRepository.findAuthorsByFullName(firstName, lastName, Utils.getPageable(page, elements));
    }

    public List<Author> getAuthorsByLastName(String lastName, int page, int elements) {
        return authorRepository.findAuthorsByLastName(lastName, Utils.getPageable(page, elements));
    }

    public List<Author> getAuthorsWithBookTitled(String title, int page, int elements) {
        List<Author> authors = new ArrayList<>();
        for (Integer i : bookRepository.getAuthorIDsWithTitle(title, Utils.getPageable(page, elements))) {
            authors.add(getAuthorByID(i));
        }
        return authors;
    }

    public Author getAuthorByBookID(int bid) {
        return authorRepository.findById(authorRepository.getAuthorIDByBookID(bid)).orElse(null);
    }

    public List<Author> getAuthorsWithBookTitled(String title, int page) {
        return getAuthorsWithBookTitled(title, page, Properties.getInstance().getPageElements());
    }

    public List<Author> getAuthorsWithBookTitled(String title) {
        List<Author> authors = new ArrayList<>();
        for (Integer i : bookRepository.getAuthorIDsWithTitle(title)) {
            authors.add(getAuthorByID(i));
        }
        return authors;
    }

    public boolean exists(Author author) {
        return authorRepository.findAuthor(author.getId(), author.getName()) != null;
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);
    }

    // ==== private methods ====
    private boolean containsID(int id) {
        return getAuthorByID(id) != null;
    }


}

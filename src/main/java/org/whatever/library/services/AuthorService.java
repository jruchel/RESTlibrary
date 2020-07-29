package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.repository.AuthorRepository;
import org.whatever.library.repository.BookRepository;
import org.whatever.library.utils.CollectionUtils;

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

    public List<Author> getAuthorsByName(String firstName) {
        return authorRepository.findAuthorsByName(firstName);
    }

    public List<Author> getAuthorsByName(String firstName, String lastName) {
        return authorCollectionUtils.getCommonObjects(authorRepository.findAuthorsByLastName(lastName), authorRepository.findAuthorsByName(firstName));
    }

    public List<Author> getAuthorsByLastName(String lastName) {
        return authorRepository.findAuthorsByLastName(lastName);
    }

    public List<Author> getAuthorsWithBookTitled(String title) {
        List<Author> authors = new ArrayList<>();
        for (Integer i : bookRepository.getAuthorIDsWithTitle(title)) {
            authors.add(getAuthorByID(i));
        }
        return authors;
    }

    public boolean exists(Author author) {
        List<Author> authorsList = (List<Author>) getAllAuthors();
        return authorsList.stream().filter(authorInList -> authorInList.equals(author)).count() > 0;
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);
    }

    // ==== private methods ====
    private boolean containsID(int id) {
        return getAuthorByID(id) != null;
    }


}

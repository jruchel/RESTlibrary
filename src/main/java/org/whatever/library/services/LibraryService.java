package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    @Autowired
    private AuthorRepository repository;

    public Iterable<Author> getAllAuthors() {
        return repository.findAll();
    }

    public Author getAuthorByID(int id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        return null;
    }

    public void save(Author author) {
        repository.save(author);
    }

    public void addBooks(Author author, Iterable<Book> books) {
        author.addBooks(books);
        repository.save(author);
    }

    public void addBook(Author author, Book book) {
        author.addBook(book);
        repository.save(author);
    }

    public boolean exists(Author author) {
        List<Author> authorsList = (List<Author>) getAllAuthors();
        if (authorsList.stream().filter(authorInList -> authorInList.equals(author)).collect(Collectors.toList()).size() > 0) {
            return true;
        }
        return false;
    }

    public Iterable<Book> getAllBooks(int aid) {
        return getAuthorByID(aid).getBibliography();
    }

    public Book getBookByID(int aid, int bid) {
        return getAuthorByID(aid).getBook(bid);
    }

}

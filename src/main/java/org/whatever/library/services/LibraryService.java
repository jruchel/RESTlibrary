package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;
import org.whatever.library.repository.AuthorRepository;
import org.whatever.library.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

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

    public void addBooks(Author author, Iterable<Book> books) {
        author.addBooks(books);
        authorRepository.save(author);
    }

    public void addBook(Author author, Book book) {
        author.addBook(book);
        authorRepository.save(author);
    }

    public boolean exists(Author author) {
        List<Author> authorsList = (List<Author>) getAllAuthors();
        if (authorsList.stream().filter(authorInList -> authorInList.equals(author)).collect(Collectors.toList()).size() > 0) {
            return true;
        }
        return false;
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);
    }

    public void deleteBookByID(int bid) {
        bookRepository.deleteById(bid);
    }

    private boolean containsID(int id) {
        return getAuthorByID(id) != null;
    }

    private boolean containsBook(int id, int bid) {
        return getBookByID(id, bid) != null;
    }

    public Iterable<Book> getAllBooks(int aid) {
        return getAuthorByID(aid).getBibliography();
    }

    public Book getBookByID(int aid, int bid) throws NullPointerException {
        return getAuthorByID(aid).getBook(bid);
    }

}

package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Book;
import org.whatever.library.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public Iterable<Book> getAllBooks() {
        return repository.findAll();
    }

    public Book getBookByID(int id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        return null;
    }

    public void save(Book book) {
        repository.save(book);
    }

    public boolean exists(Book book) {
        List<Book> booksAsList = (List<Book>) getAllBooks();
        if (booksAsList.stream().filter(b -> b.equals(book)).collect(Collectors.toList()).get(0) != null) {
            return false;
        }
        return true;
    }

}

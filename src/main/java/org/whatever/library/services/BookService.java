package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Book;
import org.whatever.library.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public List<Book> getAllBooks() {
        return (List<Book>) repository.findAll();
    }

    public Book getBookByID(int id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        return null;
    }

    public void save(Book book) {
        repository.save(book);
    }

}

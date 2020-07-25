package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Book;
import org.whatever.library.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorService authorService;

    public Book getBookByTitle(int id, String title) {
        List<Book> allBooks = (List<Book>) getAllBooks(id);

        try {
            return allBooks.stream().filter(b -> b.getTitle().equals(title)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Book> getRentedBooks() {
        return bookRepository.getRentedBooks();
    }

    public List<Book> getReservedBooks() {
        return bookRepository.getReservedBooks();
    }

    public List<Book> getRentedOrReservedBooks() {
        List<Book> books = new ArrayList<>();
        books.addAll(getRentedBooks());
        books.addAll(getReservedBooks());

        return books;
    }

    public Set<Book> getBookByTitle(String title, int limit) {
        return bookRepository.getBooksByTitle(title, limit);
    }

    public int getLibrarySize() {
        return bookRepository.getLibrarySize();
    }

    public int getBookCount() {
        return bookRepository.getBookCount();
    }

    public Iterable<Book> getAllBooks(int aid) {
        return authorService.getAuthorByID(aid).getBibliography();
    }

    public Book findByID(int bid) throws NullPointerException {
        if (bookRepository.findById(bid).isPresent()) {
            return bookRepository.findById(bid).get();
        }
        return null;
    }

    public void deleteBookByID(int bid) {
        bookRepository.deleteById(bid);
    }

    // ==== private methods ====
    private boolean containsBook(int id, int bid) {
        return findByID(bid) != null;
    }
}

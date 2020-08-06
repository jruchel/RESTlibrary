package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.whatever.library.models.Book;
import org.whatever.library.repositories.BookRepository;
import org.whatever.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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

    public Page<Book> getRentedBooks(int page, int elements) {

        return bookRepository.getRentedBooks(Utils.getPageable(page, elements));
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

    public Page<Book> getBookByTitle(String title, int page, int elements) {
        return bookRepository.getBooksByTitle(title, Utils.getPageable(page, elements));
    }

    public Page<Book> getBookByTitle(String title, int page) {
        return bookRepository.getBooksByTitle(title, Utils.getPageable(page));
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

package org.whatever.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.whatever.library.models.Author;
import org.whatever.library.models.Book;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {

    @Query(value = "SELECT author_id from books where title = ?1", nativeQuery = true)
    Page<Integer> getAuthorIDsWithTitle(String title, Pageable pageable);

    @Query(value = "SELECT author_id from books where title = ?1", nativeQuery = true)
    List<Integer> getAuthorIDsWithTitle(String title);

    @Query(value = "SELECT book_id FROM users_books_reserving", nativeQuery = true)
    Page<Book> getReservedBooks(Pageable pageable);

    @Query(value = "SELECT book_id FROM users_books_reserving", nativeQuery = true)
    List<Book> getReservedBooks();

    @Query(value = "SELECT book_id FROM users_books_renting", nativeQuery = true)
    Page<Book> getRentedBooks(Pageable pageable);

    @Query(value = "SELECT book_id FROM users_books_renting", nativeQuery = true)
    List<Book> getRentedBooks();

    @Query(value = "SELECT count(*) from books", nativeQuery = true)
    int getBookCount();

    @Query(value = "SELECT sum(in_stock) FROM library.books", nativeQuery = true)
    int getLibrarySize();

    @Query(value = "select * from books where title like %?1%", nativeQuery = true)
    Page<Book> getBooksByTitle(String title, Pageable pageable);

    @Query(value = "select library.books.author_id from library.books where library.books.id = ?1", nativeQuery = true)
    int findAuthorIDByBookID(int id);

}

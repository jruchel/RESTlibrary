package org.whatever.library.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.whatever.library.model.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query(value = "SELECT author_id from books where title = ?1", nativeQuery = true)
    List<Integer> getAuthorIDsWithTitle(String title);

    @Query(value = "SELECT book_id FROM users_books_reserving", nativeQuery = true)
    List<Book> getReservedBooks();

    @Query(value = "SELECT book_id FROM users_books_renting", nativeQuery = true)
    List<Book> getRentedBooks();

    @Query(value = "SELECT count(*) from books", nativeQuery = true)
    int getBookCount();

    @Query(value = "SELECT sum(in_stock) FROM library.books", nativeQuery = true)
    int getLibrarySize();

}

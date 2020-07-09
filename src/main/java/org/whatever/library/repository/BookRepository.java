package org.whatever.library.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.whatever.library.model.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query(value = "select author_id from books where title = ?1", nativeQuery = true)
    List<Integer> getAuthorIDsWithTitle(String title);

}

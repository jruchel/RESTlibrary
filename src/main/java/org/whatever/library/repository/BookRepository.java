package org.whatever.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.whatever.library.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
}

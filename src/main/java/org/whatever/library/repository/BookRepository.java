package org.whatever.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.whatever.library.model.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {
}

package org.whatever.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.whatever.library.model.Author;

@Repository
public interface AuthorRepository  extends CrudRepository<Author, Integer> {

}

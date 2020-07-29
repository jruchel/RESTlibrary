package org.whatever.library.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.whatever.library.model.Author;

import java.util.List;


@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer>, JpaSpecificationExecutor<Author> {

    @Query(value = "select * from authors where name like ?1%", nativeQuery = true)
    List<Author> findAuthorsByName(String firstName);

    @Query(value = "select * from authors where name like %?1", nativeQuery = true)
    List<Author> findAuthorsByLastName(String lastName);

    @Query(value = "select count(*) from authors", nativeQuery = true)
    int getAuthorCount();
}

package org.whatever.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.whatever.library.model.Author;

import java.util.List;


@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {

    @Query(value = "select * from authors where name like ?1%", nativeQuery = true)
    List<Author> findAuthorsByName(String firstName, Pageable pageable);

    @Query(value = "select * from authors where name like ?1%", nativeQuery = true)
    List<Author> findAuthorsByName(String firstName);

    @Query(value = "select * from authors where name like ?1% and name like %?2", nativeQuery = true)
    List<Author> findAuthorsByFullName(String firstName, String lastName, Pageable pageable);

    @Query(value = "select * from authors where name like ?1% and name like %?2", nativeQuery = true)
    List<Author> findAuthorsByFullName(String firstName, String lastName);

    @Query(value = "select * from authors where name like %?1", nativeQuery = true)
    List<Author> findAuthorsByLastName(String lastName);

    @Query(value = "select * from authors where name like %?1", nativeQuery = true)
    List<Author> findAuthorsByLastName(String lastName, Pageable pageable);

    @Query(value = "select count(*) from authors", nativeQuery = true)
    int getAuthorCount();

    @Query(value = "select * from authors", nativeQuery = true)
    Page<Author> findAll(Pageable pageable);

    @Query(value = "select * from authors", nativeQuery = true)
    Iterable<Author> findAll();

    @Query(value = "select * from library.authors where id = ?1 and name = ?2", nativeQuery = true)
    Author findAuthor(int id, String name);
}

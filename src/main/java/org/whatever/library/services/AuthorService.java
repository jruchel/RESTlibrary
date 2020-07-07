package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whatever.library.model.Author;
import org.whatever.library.repository.AuthorRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repository;

    public Iterable<Author> getAllAuthors() {
        return repository.findAll();
    }

    public Author getAuthorByID(int id) {
        if (repository.findById(id).isPresent())
            return repository.findById(id).get();
        return null;
    }

    public void save(Author author) {
        repository.save(author);
    }

    public boolean exists(Author author) {
        List<Author> booksAsList = (List<Author>) getAllAuthors();
        if (booksAsList.stream().filter(b -> b.equals(author)).collect(Collectors.toList()).get(0) != null) {
            return false;
        }
        return true;
    }

}

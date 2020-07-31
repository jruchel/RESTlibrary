package org.whatever.library.model;

import lombok.Getter;
import lombok.Setter;
import org.whatever.library.validation.NameConstraint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NameConstraint
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
    private List<Book> bibliography;

    public Author() {
        bibliography = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Author)) return false;
        Author a2 = (Author) obj;
        return a2.name.equals(name);
    }

    private boolean sameBooks(Set<Book> books) {
        for (Book b : books) {
            if (!this.bibliography.contains(b)) return false;
        }

        return books.size() == bibliography.size();
    }

    public Book getBook(int id) {
        List<Book> booksWithID = bibliography.stream().filter(b -> b.getId() == id).collect(Collectors.toList());
        if (booksWithID.size() > 0)
            return booksWithID.get(0);
        else throw new NoSuchElementException(String.format("Book with id %d does not exist", id));
    }

    public void addBook(Book book) {
        this.bibliography.add(book);
        book.setAuthor(this);
    }

    public void addBooks(Iterable<Book> books) {
        for (Book b : books) {
            this.addBook(b);
            b.setAuthor(this);
        }
    }

    public void removeBook(int bid) {
        bibliography.removeIf(b -> b.getId() == bid);
    }

    public void setBibliography(List<Book> bibliography) {
        this.bibliography = bibliography;
        if(bibliography.size() > 0)
        assignBooks();
    }

    private void assignBooks() {
        for (Book b : bibliography) {
            b.setAuthor(this);
        }
    }
}

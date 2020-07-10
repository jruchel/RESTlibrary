package org.whatever.library.model;

import org.whatever.library.validation.NameConstraint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NameConstraint
    @Column(name = "firstName")
    private String firstName;

    @NameConstraint
    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
    private List<Book> bibliography;

    public Author() {
        bibliography = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        Author a2 = (Author) obj;
        return a2.lastName.equals(lastName) && a2.firstName.equals(firstName);
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
        else return null;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBibliography() {
        return bibliography;
    }

    public void setBibliography(List<Book> bibliography) {
        this.bibliography = bibliography;
        assignBooks();
    }

    private void assignBooks() {
        for (Book b : bibliography) {
            b.setAuthor(this);
        }
    }
}

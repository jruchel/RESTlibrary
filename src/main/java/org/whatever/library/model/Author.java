package org.whatever.library.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
    private Set<Book> bibliography;

    public Author() {
        bibliography = new HashSet<>();
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
        return bibliography.stream().filter(b -> b.getId() == id).collect(Collectors.toList()).get(0);
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

    public Set<Book> getBibliography() {
        return bibliography;
    }

    public void setBibliography(Set<Book> bibliography) {
        this.bibliography = bibliography;
        assignBooks();
    }

    private void assignBooks() {
        for(Book b: bibliography) {
            b.setAuthor(this);
        }
    }
}

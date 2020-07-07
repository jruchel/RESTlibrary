package org.whatever.library.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue()
    private int id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "author")
    private Set<Book> bibliography;

    public Author() {

    }

    @Override
    public boolean equals(Object obj) {
        Author a2 = (Author) obj;
        return a2.lastName.equals(lastName) && a2.firstName.equals(firstName) && sameBooks(a2.bibliography);
    }

    private boolean sameBooks(Set<Book> books) {
        for (Book b : books) {
            if (!this.bibliography.contains(b)) return false;
        }

        return books.size() == bibliography.size();
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
    }
}

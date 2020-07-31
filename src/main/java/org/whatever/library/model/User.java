package org.whatever.library.model;


import lombok.Getter;
import lombok.Setter;
import org.whatever.library.payments.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String password;
    @Transient
    private String passwordConfirm;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToMany
    @JoinTable(
            name = "users_books_reserving",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private List<Book> reservedBooks;

    @ManyToMany
    @JoinTable(
            name = "users_books_renting",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private List<Book> rentedBooks;

    @ManyToMany
    private Set<Role> roles;

    public void cancelReservation(int bid) {
        reservedBooks.stream().filter(b -> b.getId() == bid).findFirst().ifPresent(b -> reservedBooks.remove(b));
    }

    public void returnBook(Book book) {
        rentedBooks.remove(book);
    }

    public void reserveBook(Book book) {
        if (book.reserve(this))
            this.reservedBooks.add(book);
    }

    public User() {
        this.roles = new HashSet<>();
        this.reservedBooks = new ArrayList<>();
        this.rentedBooks = new ArrayList<>();
    }

    public void giveRole(Role role) {
        this.roles.add(role);
    }

    public void revokeRole(String role) {
        this.roles.removeIf(r -> r.getName().equals(role));
    }
}


package org.whatever.library.models;


import org.whatever.library.payments.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Book> getReservedBooks() {
        return reservedBooks;
    }

    public void setReservedBooks(List<Book> reservedBooks) {
        this.reservedBooks = reservedBooks;
    }

    public List<Book> getRentedBooks() {
        return rentedBooks;
    }

    public void setRentedBooks(List<Book> rentedBooks) {
        this.rentedBooks = rentedBooks;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

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

    public boolean hasRole(String role) {
        for (Role r : roles) {
            if (r.getName().contains(role)) return true;
        }
        return false;
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


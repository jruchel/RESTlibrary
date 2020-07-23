package org.whatever.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String title;

    @Column
    private int inStock;

    @Column
    private int reserved;

    @Column
    private int rented;

    @JsonIgnore
    @ManyToMany(mappedBy = "reservedBooks", cascade = {CascadeType.PERSIST})
    private List<User> reservingUsers;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Book() {
        this.inStock = 1;
        this.reservingUsers = new ArrayList<>();
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getRented() {
        return rented;
    }

    public void setRented(int rented) {
        this.rented = rented;
    }


    public List<User> getReservingUsers() {
        return reservingUsers;
    }

    public void setReservingUsers(List<User> reservingUsers) {
        this.reservingUsers = reservingUsers;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Book)) return false;
        Book b2 = (Book) obj;
        return b2.getTitle().equals(title) && b2.getAuthor().equals(author);
    }

    public int getId() {
        return id;
    }

    @Override
    protected Object clone() {
        Book book = new Book();
        book.setAuthor(this.getAuthor());
        book.setTitle(this.getTitle());
        return book;
    }

    public boolean reserve(User user) {
        //Jesli uzytkownik juz wypozycza ksiazke, przerwanie
        if (reservingUsers.contains(user) /*|| rentingUsers.contains(user)*/) return false;
        if (inStock < 1) return false;
        inStock--;
        reserved++;
        reservingUsers.add(user);
        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addToStock(int amount) {
        inStock += amount;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getInStock() {
        return inStock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}

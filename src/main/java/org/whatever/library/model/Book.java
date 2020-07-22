package org.whatever.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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
    @ManyToOne()
    @JoinColumn(name = "author_id")
    private Author author;

    public Book() {
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

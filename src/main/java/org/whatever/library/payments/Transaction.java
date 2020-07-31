package org.whatever.library.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.whatever.library.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Transient
    private Card card;
    private double amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private String chargeID;
    private long time;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction(Card card, double amount, Currency currency) {
        this();
        this.card = card;
        this.amount = amount;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transaction() {
        chargeID = "";
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTime(Date date) {
        setTime(date.getTime());
    }

    public String getChargeID() {
        return chargeID;
    }

    public void setChargeID(String chargeID) {
        this.chargeID = chargeID;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}

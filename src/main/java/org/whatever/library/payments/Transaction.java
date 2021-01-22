package org.whatever.library.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.whatever.library.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getChargeID() {
        return chargeID;
    }

    public void setChargeID(String chargeID) {
        this.chargeID = chargeID;
    }

    public long getTime() {
        return time;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Refund> getRefunds() {
        return refunds;
    }

    public void setRefunds(List<Refund> refunds) {
        this.refunds = refunds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
    private boolean refunded;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaction")
    private List<Refund> refunds;

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

    public Transaction() {
        chargeID = "";
        this.refunds = new ArrayList<>();
        this.refunded = false;
        this.time = new Date().getTime();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTime(Date date) {
        this.time = date.getTime();
    }


}

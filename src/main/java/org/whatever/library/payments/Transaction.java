package org.whatever.library.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.whatever.library.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
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

    public Transaction() {
        chargeID = "";
    }



}

package org.whatever.library.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.whatever.library.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

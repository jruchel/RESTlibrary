package org.whatever.library.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "refunds")
public class Refund {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Transaction transaction;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String message;
    private String reason;

    public Refund() {
        this.status = Status.Pending;
    }

    public void process(boolean accept, String reason) {
        if (accept) this.status = Status.Approved;
        else this.status = Status.Rejected;

        if (this.status.equals(Status.Approved)) this.transaction.setRefunded(true);
        this.reason = reason;

    }

    public enum Status {
        Pending, Approved, Rejected
    }

}



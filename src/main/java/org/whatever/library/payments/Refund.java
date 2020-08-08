package org.whatever.library.payments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "refunds")
@Getter
@Setter
public class Refund {

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



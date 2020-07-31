package org.whatever.library.payments;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Card {

    private int id;
    private long number;
    private int expirationMonth;
    private int expirationYear;
    private int cvc;

    public Card(long number, int expirationMonth, int expirationYear, int cvc) {
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cvc = cvc;
    }
}

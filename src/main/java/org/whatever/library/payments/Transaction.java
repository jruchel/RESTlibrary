package org.whatever.library.payments;


public class Transaction {

    private Card card;
    private double amount;
    private Currency currency;

    public Transaction(Card card, double amount, Currency currency) {
        this.card = card;
        this.amount = amount;
        this.currency = currency;
    }

    public Transaction() {

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

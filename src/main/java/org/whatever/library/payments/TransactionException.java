package org.whatever.library.payments;

import java.beans.Transient;

public class TransactionException extends Exception {

    public TransactionException(String msg) {
        super(msg);
    }

    public TransactionException() {
        super("Error occurred while processing the transaction");
    }
}

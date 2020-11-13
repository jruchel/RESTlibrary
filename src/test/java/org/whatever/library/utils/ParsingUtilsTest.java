package org.whatever.library.utils;


import org.junit.Test;
import org.springframework.data.domain.Page;
import org.whatever.library.payments.Card;
import org.whatever.library.payments.Currency;
import org.whatever.library.payments.Transaction;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class ParsingUtilsTest {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4);
        Iterable<Integer> iter = integers;
        Page<Integer> page = (Page<Integer>) iter;

        try {
            System.out.println(ParsingUtils.objectToJSON(page));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void transactionJSON() {


    }

}
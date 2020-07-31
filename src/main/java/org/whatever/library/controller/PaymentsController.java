package org.whatever.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.payments.Transaction;

import org.whatever.library.services.PaymentService;



@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @CrossOrigin
    @PostMapping("/charge")
    public String charge(@RequestBody Transaction transactionDetails) {
        try {
            paymentService.charge(transactionDetails.getCard(), transactionDetails.getAmount(), transactionDetails.getCurrency());
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}

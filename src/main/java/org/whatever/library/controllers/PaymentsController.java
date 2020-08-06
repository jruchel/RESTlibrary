package org.whatever.library.controllers;

import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.models.User;
import org.whatever.library.payments.Transaction;

import org.whatever.library.services.PaymentService;
import org.whatever.library.services.TransactionService;
import org.whatever.library.services.UserService;


@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @CrossOrigin
    @PostMapping("/card")
    public String charge(@RequestBody Transaction transactionDetails) {
        Charge charge;
        try {
            charge = paymentService.charge(transactionDetails.getCard(), transactionDetails.getAmount(), transactionDetails.getCurrency());
        } catch (Exception ex) {
            return ex.getMessage();
        }
        try {
            User user = userService.getCurrentUser();
            transactionDetails.setChargeID(charge.getId());
            transactionDetails.setTime(charge.getCreated());
            transactionDetails.setUser(user);
            transactionService.save(transactionDetails);
            return "success";
        } catch (Exception e) {
            refund(charge.getId());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @PostMapping("/refund")
    public String refund(@RequestBody String chargeID) {
        try {
            paymentService.refund(chargeID);
            return "success";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

}

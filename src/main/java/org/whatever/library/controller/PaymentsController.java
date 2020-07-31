package org.whatever.library.controller;

import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.model.User;
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
        try {
            User user = userService.getCurrentUser();
            Charge charge = paymentService.charge(transactionDetails.getCard(), transactionDetails.getAmount(), transactionDetails.getCurrency());
            transactionDetails.setChargeID(charge.getId());
            transactionDetails.setTime(charge.getCreated());
            transactionDetails.setUser(user);
            transactionService.save(transactionDetails);
            return "success";
        } catch (Exception e) {
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

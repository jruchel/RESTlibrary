package org.whatever.library.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.whatever.library.Properties;
import org.whatever.library.models.Role;
import org.whatever.library.models.User;
import org.whatever.library.payments.Card;
import org.whatever.library.payments.Currency;
import org.whatever.library.payments.Refund;
import org.whatever.library.payments.Transaction;

import org.whatever.library.security.IllegalActionException;
import org.whatever.library.services.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @CrossOrigin
    @PostMapping("/user/card")
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
            try {
                refundService.refund(charge.getId());
                return "Transaction error, transaction has been refunded";
            } catch (StripeException ex) {
                Map<String, String> params = new HashMap<>();
                params.put("chargeid", charge.getId());
                params.put("message", "Payment error");
                refund(params);
                return "Transaction and refund error, refund has been requested";
            }
        }
    }

    @CrossOrigin
    @PostMapping("/user/subscribe")
    public String subscribe(@RequestBody Card card) {
        User user = userService.getCurrentUser();
        Role role = roleService.getRoleByName("ROLE_SUBSCRIBER");
        if (user.getRoles().contains(role)) return "You are already subscribed";

        Transaction subscription = new Transaction();
        subscription.setCard(card);
        subscription.setUser(user);
        subscription.setDescription("Subscription payment");
        subscription.setAmount(Properties.getInstance().getSubscriptionPriceMonthly());
        subscription.setCurrency(Currency.USD);
        String result = charge(subscription);
        if (result.equals("success")) {
            user.giveRole(role);
            userService.save(user);
        }
        return result;
    }

    @CrossOrigin
    @PostMapping("/user/refund")
    public String refund(@RequestBody Map<String, String> params) {
        Transaction transaction = transactionService.findByChargeID(params.get("chargeid"));
        String message = params.get("message");
        if (transaction.getRefunds().stream().anyMatch(refund -> refund.getStatus().equals(Refund.Status.Pending)))
            return "There is already a pending refund for this transaction";

        try {
            refundService.requestRefund(transaction, message);
            return "success";
        } catch (IllegalActionException e) {
            return e.getMessage();
        }
    }

    @CrossOrigin
    @PostMapping("/moderator/refund")
    public String resolveRefund(@RequestBody Map<String, String> params) {
        String refundID = params.get("rid");
        String reason = params.get("reason");
        String decision = params.get("decision");

        try {
            refundService.performRefund(Integer.parseInt(refundID), reason, Boolean.parseBoolean(decision));
            return "success";
        } catch (StripeException e) {
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping("/currencies")
    public Currency[] getCurrencies() {
        return Currency.values();
    }

    @CrossOrigin
    @GetMapping("/moderator/refunds")
    public List<Refund> getPendingRefunds() {
        return refundService.getRefundsWithStatus("Pending");
    }

    @CrossOrigin
    @GetMapping("/user/refunds")
    public List<Refund> getUserRefunds() {
        User user = userService.getCurrentUser();
        List<Refund> refunds = new ArrayList<>();

        for (Transaction transaction : user.getTransactions()) {
            refunds.addAll(transaction.getRefunds());
        }
        return refunds;
    }

}

package org.whatever.library.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.whatever.library.payments.Refund;

import java.util.HashMap;
import java.util.List;

import org.whatever.library.payments.Transaction;
import org.whatever.library.repositories.RefundRepository;
import org.whatever.library.security.IllegalActionException;

import java.util.Map;
import java.util.Optional;

@Service
public class RefundService {

    public RefundService(@Value("${STRIPE_PUBLIC_KEY}") String publicKey, TransactionService transactionService) {
        Stripe.apiKey = publicKey;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RefundRepository refundRepository;

    public List<Refund> getRefundsWithStatus(String status) {
        return refundRepository.getRefundsWithStatus(status);
    }

    private void save(Refund refund) {
        refundRepository.save(refund);
    }

    public Refund findByID(int id) {
        Optional<Refund> optional = refundRepository.findById(id);
        return optional.orElse(null);
    }

    public com.stripe.model.Refund refund(Charge charge) throws StripeException {
        return refund(charge.getId());
    }

    public com.stripe.model.Refund refund(String chargeID) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("charge", chargeID);
        com.stripe.model.Refund refund = com.stripe.model.Refund.create(params);
        return refund;
    }

    public com.stripe.model.Refund performRefund(int id, String reason, boolean decision) throws StripeException {
        Refund refund = findByID(id);
        refund.process(decision, reason);
        com.stripe.model.Refund resultRefund = null;
        if (decision) resultRefund = refund(refund.getTransaction().getChargeID());
        save(refund);
        return resultRefund;
    }

    public void requestRefund(Transaction transaction, String message) throws IllegalActionException {
        if (userService.getCurrentUser().getId() != transaction.getUser().getId())
            throw new IllegalActionException("This transaction is not linked to your account");

        Refund refund = new Refund();
        refund.setMessage(message);
        refund.setTransaction(transaction);

        refundRepository.save(refund);
    }
}

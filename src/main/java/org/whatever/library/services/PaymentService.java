package org.whatever.library.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.whatever.library.payments.Card;
import org.whatever.library.payments.Currency;
import org.whatever.library.payments.TransactionException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PaymentService {

    private String publicKey;

    public PaymentService(@Value("${STRIPE_PUBLIC_KEY}") String publicKey) {
        Stripe.apiKey = publicKey;
    }

    @Autowired
    private TransactionService transactionService;

    private Charge charge(String token, double amount, Currency currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (amount * 100));
        params.put("currency", currency.toString());
        params.put("source", token);
        return Charge.create(params);
    }

    public Charge charge(Card card, double amount, Currency currency) throws StripeException, TransactionException {
        return charge(getToken(card), amount, currency);
    }

    public Refund refund(Charge charge) throws StripeException {
        return refund(charge.getId());
    }

    public Refund refund(String chargeID) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("charge", chargeID);
        Refund refund = Refund.create(params);
        transactionService.deleteTransaction(chargeID);
        return refund;
    }

    private String getToken(Card card) throws StripeException, TransactionException {
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put("number", card.getNumber());
        cardMap.put("exp_month", card.getExpirationMonth());
        cardMap.put("exp_year", card.getExpirationYear());
        cardMap.put("cvc", card.getCvc());
        Map<String, Object> params = new HashMap<>();
        params.put("card", cardMap);
        Token token = Token.create(params);
        String result = token.toString();
        result = result.split("JSON: ")[1];
        if (isError(result)) throw new TransactionException(getErrorMessage(result));

        return getToken(result);
    }

    private String getToken(String json) {
        String[] props = json.split("\n");

        for (String s : props) {
            if (s.contains("id") && s.contains("tok")) {
                return s.split(": ")[1].replaceAll("\"", "").replaceAll(",", "");
            }
        }
        return "";
    }

    private String getErrorMessage(String transactionResult) {
        String errorRegex = ".*\\\"code\\\"\\:\\ \\\"(\\w+)\\\".*";
        Pattern pattern = Pattern.compile(errorRegex);
        Matcher matcher = pattern.matcher(transactionResult);

        if (matcher.matches())
            return matcher.group(1);
        else return "";
    }

    private boolean isError(String transactionResult) {
        return transactionResult.contains("\"error\":");
    }

}

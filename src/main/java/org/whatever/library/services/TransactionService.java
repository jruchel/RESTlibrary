package org.whatever.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whatever.library.payments.Transaction;
import org.whatever.library.repository.TransactionRepository;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    public Transaction findByChargeID(String chargeID) {
        return transactionRepository.findByChargeID(chargeID);
    }

    public void deleteTransaction(String chargeID) {
        transactionRepository.deleteTransaction(chargeID);
    }

    public List<Transaction> findChargeByTime(long time) {
        return transactionRepository.findChargeByTime(time);
    }


    public List<Transaction> findChargeByAmount(double amount) {
        return transactionRepository.findChargeByAmount(amount);
    }


    public List<Transaction> findChargeByCurrency(Currency currency) {
        return transactionRepository.findChargeByCurrency(currency.toString());
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction findByID(int id) {
        Optional<Transaction> optional = transactionRepository.findById(id);
        return optional.orElse(null);
    }
}

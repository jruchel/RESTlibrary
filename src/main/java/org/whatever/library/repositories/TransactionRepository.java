package org.whatever.library.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.whatever.library.payments.Transaction;

import java.util.List;

@Transactional
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query(value = "select * from library.transactions where chargeid = ?1", nativeQuery = true)
    Transaction findByChargeID(String chargeID);

    @Query(value = "select user_id from transactions where id = ?1", nativeQuery = true)
    int getUserIDWithTransaction(int transactionID);

    @Query(value = "select * from library.transactions where time = ?1", nativeQuery = true)
    List<Transaction> findChargeByTime(long time);

    @Query(value = "select * from library.transactions where amount = ?1", nativeQuery = true)
    List<Transaction> findChargeByAmount(double amount);

    @Query(value = "select * from library.transactions where currency = ?1", nativeQuery = true)
    List<Transaction> findChargeByCurrency(String currency);

    @Modifying
    @Query(value = "delete from library.transactions where chargeid = ?1", nativeQuery = true)
    void deleteTransaction(String chargeID);

}

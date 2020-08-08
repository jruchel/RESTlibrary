package org.whatever.library.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.whatever.library.payments.Refund;

import java.util.List;

public interface RefundRepository extends CrudRepository<Refund, Integer> {

    @Query(value = "select * from refunds where status = ?1", nativeQuery = true)
    List<Refund> getRefundsWithStatus(String status);

}

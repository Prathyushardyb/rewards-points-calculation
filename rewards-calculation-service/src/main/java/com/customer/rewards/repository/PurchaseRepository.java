package com.customer.rewards.repository;

import java.sql.Timestamp;
import java.util.List;

import com.customer.rewards.entity.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
     List<Purchase> findAllByCustomerIdAndPurchaseDateBetween(Long customerId, Timestamp from, Timestamp to);
}

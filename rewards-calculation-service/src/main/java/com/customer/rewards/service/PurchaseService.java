package com.customer.rewards.service;

import com.customer.rewards.model.PurchaseTo;

import java.sql.Timestamp;
import java.util.List;

public interface PurchaseService {

    PurchaseTo getPurchaseById(Long id);

    PurchaseTo save(PurchaseTo purchase);

    void delete(Long purchaseId);

    void updatePurchase(Long id, PurchaseTo purchase);

    List<PurchaseTo> getAllPurchasesByCustomerIdAndPurchaseDateBetween(Long customerId, Timestamp from, Timestamp to);

}

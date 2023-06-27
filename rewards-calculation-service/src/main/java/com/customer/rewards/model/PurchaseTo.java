package com.customer.rewards.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PurchaseTo {
    private Long id;
    private Long customerId;
    private Timestamp purchaseDate;
    private double purchaseAmount;
}

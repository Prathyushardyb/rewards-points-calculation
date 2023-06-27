package com.customer.rewards.utils;

import com.customer.rewards.entity.Customer;
import com.customer.rewards.entity.Purchase;
import com.customer.rewards.model.PurchaseTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.customer.rewards.model.CustomerTo;

public abstract class ObjectMapperUtils {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Purchase convertPurchaseDtoToPurchaseEntity(PurchaseTo purchaseTo) {
        return objectMapper.convertValue(purchaseTo, Purchase.class);
    }

    public static PurchaseTo convertPurchaseEntityToPurchaseDto(Purchase purchase) {
        return objectMapper.convertValue(purchase, PurchaseTo.class);
    }

    public static Customer convertCustomerDtoToCustomerEntity(CustomerTo customerTo) {
        return objectMapper.convertValue(customerTo, Customer.class);
    }

    public static CustomerTo convertCustomerEntityToCustomerDto(Customer customer) {
        return objectMapper.convertValue(customer, CustomerTo.class);
    }

}

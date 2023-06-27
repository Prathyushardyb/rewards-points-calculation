package com.customer.rewards.service;

import com.customer.rewards.model.CustomerTo;

public interface CustomerService {

    CustomerTo getCustomerById(Long id);

    CustomerTo save(CustomerTo customer);
}

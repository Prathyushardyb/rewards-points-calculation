package com.customer.rewards.service;

import com.customer.rewards.entity.Customer;
import com.customer.rewards.exception.NotFoundException;
import com.customer.rewards.model.CustomerTo;
import com.customer.rewards.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.customer.rewards.utils.ObjectMapperUtils.convertCustomerDtoToCustomerEntity;
import static com.customer.rewards.utils.ObjectMapperUtils.convertCustomerEntityToCustomerDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerTo getCustomerById(Long id) {
        Optional<Customer> existingCustomer = customerRepository.findById(id);
        if (!existingCustomer.isPresent()) {
            String message = String.format("Customer does not exist with id - %s.", id);
            log.warn(message);
            throw new NotFoundException(message);
        }
        return convertCustomerEntityToCustomerDto(existingCustomer.get());
    }

    @Override
    public CustomerTo save(CustomerTo customer) {
        return convertCustomerEntityToCustomerDto(customerRepository.save(convertCustomerDtoToCustomerEntity(customer)));
    }
}

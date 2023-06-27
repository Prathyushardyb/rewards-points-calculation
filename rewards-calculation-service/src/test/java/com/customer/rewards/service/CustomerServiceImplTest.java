package com.customer.rewards.service;

import com.customer.rewards.entity.Customer;
import com.customer.rewards.exception.NotFoundException;
import com.customer.rewards.repository.CustomerRepository;
import com.customer.rewards.model.CustomerTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    private CustomerServiceImpl customerServiceImpl;

    @Mock
    private CustomerRepository customerRepository;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
        customerServiceImpl = new CustomerServiceImpl(customerRepository);
    }

    @Test
    public void getCustomerById() {
        //given
        Long customerId = 1L;
        Customer mockedCustomer = mockCustomer(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockedCustomer));

        //then
        CustomerTo customerTo = customerServiceImpl.getCustomerById(customerId);
        assertEquals(mockedCustomer.getId(), customerTo.getId());
        assertEquals(mockedCustomer.getCustomerName(), customerTo.getCustomerName());
    }

    @Test
    public void saveCustomer() {
        //given
        Long customerId = 1L;
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer(customerId));

        CustomerTo customerTo = customerServiceImpl.save(mockCustomerTo());
        assertEquals(customerId, customerTo.getId());
    }

    @Test
    public void getCustomerByIdShouldThrowNotFoundExceptionIfCustomerNotExistWithTheId() {
        //given
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> customerServiceImpl.getCustomerById(1L));
    }

    private Customer mockCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerName("test user");
        customer.setId(customerId);
        return customer;
    }

    private CustomerTo mockCustomerTo() {
        CustomerTo customerTo = new CustomerTo();
        customerTo.setCustomerName("test user");
        return customerTo;
    }

}

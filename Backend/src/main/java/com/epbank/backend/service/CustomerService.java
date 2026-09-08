package com.epbank.backend.service;

import org.springframework.stereotype.Service;

import com.epbank.backend.model.Customer;
import com.epbank.backend.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer registerCustomer(Customer customer){

        if(customerRepository.findByEmail(customer.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already registered");
        }
        return customerRepository.save(customer);
    }
}

package com.epbank.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.epbank.backend.dto.RegisterRequest;
import com.epbank.backend.model.Customer;
import com.epbank.backend.repository.CustomerRepository;
import com.epbank.backend.dto.CustomerResponse;
import com.epbank.backend.dto.LoginRequest;
import com.epbank.backend.dto.LoginResponse;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder){
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse loginCustomer(LoginRequest request){
        Customer customer = customerRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(), customer.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        LoginResponse response = new LoginResponse();

        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());

        return response;
    }

    public CustomerResponse registerCustomer(RegisterRequest request){

        if(customerRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        customer.setPasswordHash(hashedPassword);

        // Save Customer to PostgreSQL.
        Customer savedCustomer = customerRepository.save(customer);

        //Build the safe response.
        CustomerResponse response = new CustomerResponse();

        response.setId(savedCustomer.getId());
        response.setFirstName(savedCustomer.getFirstName());
        response.setLastName(savedCustomer.getLastName());
        response.setEmail(savedCustomer.getEmail());
        response.setCreatedAt(savedCustomer.getCreatedAt());

        return response;
    }
}

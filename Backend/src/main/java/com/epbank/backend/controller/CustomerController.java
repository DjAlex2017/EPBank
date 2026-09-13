package com.epbank.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epbank.backend.dto.RegisterRequest;
import com.epbank.backend.dto.CustomerResponse;
import com.epbank.backend.dto.LoginRequest;
import com.epbank.backend.dto.LoginResponse;
import com.epbank.backend.service.CustomerService;

/* Annotation definitions
@RestController - This class handles HTTP requests and returns data, usually JSON.
@RequestMapping() - gives the controller a base URL, so everything inside starts with ("/api/customers")
@PostMapping() - This method handles POST /api/customers/register
@RequestBody - Spring takes incoming JSON and converts it into a customer object */

@RestController 
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public CustomerResponse registerCustomer(@RequestBody RegisterRequest request){
        return customerService.registerCustomer(request);
    }

    @PostMapping("/login")
    public LoginResponse loginCustomer(@RequestBody LoginRequest request){
        return customerService.loginCustomer(request);
    }
}

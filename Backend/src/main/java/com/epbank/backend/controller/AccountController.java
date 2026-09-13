package com.epbank.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epbank.backend.dto.AccountResponse;
import com.epbank.backend.dto.OpenAccountRequest;
import com.epbank.backend.service.AccountService;

@RestController 
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/open")
    public AccountResponse openAccount(@RequestBody OpenAccountRequest request){
        return accountService.openAccount(request);
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountResponse> getAccountsByCustomer(@PathVariable Long customerId){
        return accountService.getAccountsByCustomer(customerId);
    }
}

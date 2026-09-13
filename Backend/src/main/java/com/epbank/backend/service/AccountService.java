package com.epbank.backend.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.epbank.backend.dto.AccountResponse;
import com.epbank.backend.dto.OpenAccountRequest;
import com.epbank.backend.model.Account;
import com.epbank.backend.model.Customer;
import com.epbank.backend.repository.AccountRepository;
import com.epbank.backend.repository.CustomerRepository;

@Service 
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository){
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    private String generateAccountNumber(){
        String accountNumber;

        do{
            long number = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
            accountNumber = Long.toString(number);
        }while(accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    public AccountResponse openAccount(OpenAccountRequest request){
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);
        AccountResponse response = new AccountResponse();

        response.setId(savedAccount.getId());
        response.setAccountNumber(savedAccount.getAccountNumber());
        response.setAccountType(savedAccount.getAccountType());
        response.setBalance(savedAccount.getBalance());
        response.setStatus(savedAccount.getStatus());
        response.setCreatedAt(savedAccount.getCreatedAt());

        return response;
    }
    public List<AccountResponse> getAccountsByCustomer(Long customerId){
        if(!customerRepository.existsById(customerId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");

        }

        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        return accounts.stream().map(account -> {AccountResponse response = new AccountResponse();

            response.setId(account.getId());
            response.setAccountNumber(account.getAccountNumber());
            response.setAccountType(account.getAccountType());
            response.setBalance(account.getBalance());
            response.setStatus(account.getStatus());
            response.setCreatedAt(account.getCreatedAt());

            return response;

        })
        .toList();
    }
}

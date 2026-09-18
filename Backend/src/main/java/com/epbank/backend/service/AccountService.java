package com.epbank.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.epbank.backend.dto.AccountResponse;
import com.epbank.backend.dto.DepositRequest;
import com.epbank.backend.dto.OpenAccountRequest;
import com.epbank.backend.dto.WithdrawRequest;
import com.epbank.backend.dto.TransferRequest;
import com.epbank.backend.dto.TransactionResponse;

import com.epbank.backend.model.Account;
import com.epbank.backend.model.AccountStatus;
import com.epbank.backend.model.Customer;
import com.epbank.backend.model.Transaction;
import com.epbank.backend.model.TransactionType;

import com.epbank.backend.repository.AccountRepository;
import com.epbank.backend.repository.CustomerRepository;
import com.epbank.backend.repository.TransactionRepository;




@Service 
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
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

    @Transactional 
    public AccountResponse deposit(DepositRequest request){
         Account account = accountRepository.findById(request.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

         if(request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deposit amount must be greater than zero");
        }
         
         if(account.getStatus() != AccountStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot deposit into a closed account");
        }

         account.setBalance(account.getBalance().add(request.getAmount()));

         Account savedAccount = accountRepository.save(account);

         AccountResponse response = new AccountResponse();
         Transaction transaction = new Transaction();

         transaction.setTransactionType(TransactionType.DEPOSIT);
         transaction.setAmount(request.getAmount());
         transaction.setAccount(savedAccount);

         transactionRepository.save(transaction);

         response.setId(savedAccount.getId());
         response.setAccountNumber(savedAccount.getAccountNumber());
         response.setAccountType(savedAccount.getAccountType());
         response.setBalance(savedAccount.getBalance());
         response.setStatus(savedAccount.getStatus());
         response.setCreatedAt(savedAccount.getCreatedAt());

         return response;
    }

    @Transactional 
    public AccountResponse withdraw(WithdrawRequest request){
        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdrawal amount must be greater than zero");
        }

        if(account.getStatus() != AccountStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot withdraw from a closed account");
        }

        if(request.getAmount().compareTo(account.getBalance()) > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Account savedAccount = accountRepository.save(account);

        AccountResponse response = new AccountResponse();

        Transaction transaction = new Transaction();

        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(request.getAmount());
        transaction.setAccount(savedAccount);

        transactionRepository.save(transaction);

        response.setId(savedAccount.getId());
        response.setAccountNumber(savedAccount.getAccountNumber());
        response.setAccountType(savedAccount.getAccountType());
        response.setBalance(savedAccount.getBalance());
        response.setStatus(savedAccount.getStatus());
        response.setCreatedAt(savedAccount.getCreatedAt());

        return response;

    }
    
     @Transactional 
    public AccountResponse transfer(TransferRequest request){
        Account fromAccount = accountRepository.findById(request.getFromAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source account not found"));

        Account toAccount = accountRepository.findById(request.getToAccountId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination account not found"));

        if(request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer amount must be greater than zero");
        }

        if(fromAccount.getStatus() != AccountStatus.ACTIVE || toAccount.getStatus() != AccountStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both accounts must be active");
        }

        if(request.getAmount().compareTo(fromAccount.getBalance()) > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        if(fromAccount.getId().equals(toAccount.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer to the same account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));

        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

       
        Account savedFromAccount = accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        AccountResponse response = new AccountResponse();
        
        Transaction transaction = new Transaction();

        transaction.setTransactionType(TransactionType.TRANSFER_OUT);
        transaction.setAmount(request.getAmount());
        transaction.setAccount(savedFromAccount);

        transactionRepository.save(transaction);

        Transaction incomingTransaction = new Transaction();

        incomingTransaction.setTransactionType(TransactionType.TRANSFER_IN);
        incomingTransaction.setAmount(request.getAmount());
        incomingTransaction.setAccount(toAccount);

        transactionRepository.save(incomingTransaction);
        

        response.setId(savedFromAccount.getId());
        response.setAccountNumber(savedFromAccount.getAccountNumber());
        response.setAccountType(savedFromAccount.getAccountType());
        response.setBalance(savedFromAccount.getBalance());
        response.setStatus(savedFromAccount.getStatus());
        response.setCreatedAt(savedFromAccount.getCreatedAt());

        return response;
    }

    public List<TransactionResponse> getTransactionsByAccount(Long accountId){
        if(!accountRepository.existsById(accountId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
            List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

            return transactions.stream().map(transaction -> {TransactionResponse response = new TransactionResponse();
                
            response.setId(transaction.getId());
            response.setTransactionType(transaction.getTransactionType());
            response.setAmount(transaction.getAmount());
            response.setCreatedAt(transaction.getCreatedAt());
            response.setAccountId(transaction.getAccount().getId());

            return response;

            }).toList();
    }
}

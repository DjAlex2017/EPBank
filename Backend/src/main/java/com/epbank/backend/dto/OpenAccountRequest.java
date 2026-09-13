package com.epbank.backend.dto;

import com.epbank.backend.model.AccountType;

public class OpenAccountRequest {
    private Long customerId;
    private AccountType accountType;
    
    public Long getCustomerId(){
        return customerId;
    }
    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }
    public AccountType getAccountType(){
        return accountType;
    }
    public void setAccountType(AccountType accountType){
        this.accountType = accountType;
    }
}

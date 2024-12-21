package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accounts;

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public Account get(String username) {
        return accounts.findByUsername(username);
    }

    public Account get(int id) {
        return accounts.findById(id).orElse(null);
    }

    public Account create(Account newUser) {
        return accounts.save(newUser);
    }
}

package com.example.account;

import com.example.account.Account;
import com.example.account.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountService {
    
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(String username, String password){
        Account newAccount = accountRepository.findAccountByUsernameAndPassword(username, password);
        accountRepository.save(newAccount);
        return newAccount;
    }

    public Account loginAccount(String username, String password){
        return accountRepository.login(username, password);
    }

    public Account checkAccountExistsByUsernameAndPassword(String username, String password){
        return accountRepository.findAccountByUsernameAndPassword(username, password);
    }

    public Account checkAccountExistsById(int id){
        return accountRepository.findAccountById(id);
    }
}

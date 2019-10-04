package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Account;
import com.primeholding.coenso.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Optional<Account> get(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> get(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public List<Account> get() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> create(Account account) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(account.getEmail());
        if (optionalAccount.isPresent()) {
            return Optional.empty();
        }

        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));

        return Optional.of(accountRepository.save(account));
    }

    @Override
    public Optional<Account> update(Account account) {
        return Optional.of(accountRepository.save(account));
    }

    @Override
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }
}

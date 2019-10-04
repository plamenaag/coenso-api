package com.primeholding.coenso.service;

import com.primeholding.coenso.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> get(Integer id);

    Optional<Account> get(String email);

    List<Account> get();

    Optional<Account> create(Account account);

    Optional<Account> update(Account account);

    void delete(Integer id);
}

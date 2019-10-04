package com.primeholding.coenso.security.service;

import com.primeholding.coenso.entity.Account;
import com.primeholding.coenso.repository.AccountRepository;
import com.primeholding.coenso.security.InternalAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String name) {
        Account probe = new Account();
        probe.setEmail(name);

        Optional<Account> optionalUser = accountRepository.findOne(Example.of(probe));

        return optionalUser.map(InternalAccount::create)
                .orElseThrow(() -> new UsernameNotFoundException(name));
    }
}

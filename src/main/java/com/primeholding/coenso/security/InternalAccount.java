package com.primeholding.coenso.security;

import com.primeholding.coenso.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

public class InternalAccount implements UserDetails {
    private final Integer id;
    private final String email;
    private final String password;

    private InternalAccount(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static InternalAccount create(@NotNull Account account) {
        return new InternalAccount(
                account.getId(),
                account.getEmail(),
                account.getPassword()
        );
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

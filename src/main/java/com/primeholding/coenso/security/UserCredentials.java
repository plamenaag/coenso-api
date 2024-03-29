package com.primeholding.coenso.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserCredentials {
    @Email
    @NotEmpty
    @Size(min = 2, max = 255)
    private String email;

    @NotEmpty
    @Size(min = 2, max = 255)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

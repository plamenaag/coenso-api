package com.primeholding.coenso.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class AccountPostModel {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 2, max = 255)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 255)
    private String lastName;

    @NotEmpty
    @Size(min = 2, max = 255)
    private String password;
}

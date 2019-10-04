package com.primeholding.coenso.model.smtp;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class EmailModel {
    @Email
    @NotNull
    private String email;
}

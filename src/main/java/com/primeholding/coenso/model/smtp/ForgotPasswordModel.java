package com.primeholding.coenso.model.smtp;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ForgotPasswordModel {
    @NotBlank
    @Size(min = 4, max = 50)
    private String resetCode;
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Size(min = 2, max = 255)
    private String newPassword;
}

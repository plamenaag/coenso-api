package com.primeholding.coenso.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Setter
@Getter
public class EmployeePatchModel {
    @NotBlank
    @Size(min = 2, max = 40)
    private String firstName;

    @Size(min = 2, max = 40)
    @NotBlank
    private String lastName;

    @Email
    private String email;

    @PositiveOrZero
    private Integer departmentId;
}

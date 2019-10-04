package com.primeholding.coenso.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class EmployeePostModel {
    @NotEmpty
    @Size(min = 2, max = 40)
    private String firstName;
    @NotEmpty
    @Size(min = 2, max = 40)
    private String lastName;
    @NotEmpty
    @Email
    private String email;
    @NotNull
    private Integer departmentId;
}

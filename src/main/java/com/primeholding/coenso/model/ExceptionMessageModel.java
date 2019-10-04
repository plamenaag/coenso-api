package com.primeholding.coenso.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionMessageModel {
    @NotEmpty
    private String message;
}

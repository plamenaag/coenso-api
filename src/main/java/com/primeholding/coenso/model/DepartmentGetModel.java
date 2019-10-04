package com.primeholding.coenso.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class DepartmentGetModel {
    @NotEmpty
    private Integer id;

    @NotEmpty
    private String name;
}
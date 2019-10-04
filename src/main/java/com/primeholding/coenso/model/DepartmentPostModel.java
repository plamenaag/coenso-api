package com.primeholding.coenso.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class DepartmentPostModel {
    @NotEmpty
    @Size(min = 2, max = 255)
    private String name;
}

package com.primeholding.coenso.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Getter
@Setter
public class FieldTypeGetModel {
    @NotNull
    private Integer id;

    @NotEmpty
    private String name;
}

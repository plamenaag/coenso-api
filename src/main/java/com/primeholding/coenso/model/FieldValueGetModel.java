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
public class FieldValueGetModel {
    @NotNull
    private Integer id;

    @NotEmpty
    private String value;

    @NotEmpty
    private Integer fieldTypeId;
}

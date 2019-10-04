package com.primeholding.coenso.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@Setter
@Getter
public class FieldValuePostModel {
    @NotEmpty
    @NotBlank
    private String value;


    private Integer fieldId;

    @NotNull
    @Positive
    private Integer fieldTypeId;
}

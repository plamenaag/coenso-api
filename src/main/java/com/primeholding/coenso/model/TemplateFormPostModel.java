package com.primeholding.coenso.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class TemplateFormPostModel {
    @NotEmpty
    @Size(min = 2, max = 255)
    @NotBlank
    private String title;
}

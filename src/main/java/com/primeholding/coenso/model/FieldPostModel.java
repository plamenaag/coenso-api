package com.primeholding.coenso.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class FieldPostModel {
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 255)
    private String name;

    private boolean isRequired;

    private Long templateFormId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean required) {
        isRequired = required;
    }

    public Long getTemplateFormId() {
        return templateFormId;
    }

    public void setTemplateFormId(Long templateFormId) {
        this.templateFormId = templateFormId;
    }
}

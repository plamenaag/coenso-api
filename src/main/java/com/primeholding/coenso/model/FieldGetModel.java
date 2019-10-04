package com.primeholding.coenso.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class FieldGetModel {
    @NotNull
    private Integer id;

    @NotEmpty
    private String name;

    @NotNull
    private Integer order;

    private boolean isRequired;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean required) {
        isRequired = required;
    }
}

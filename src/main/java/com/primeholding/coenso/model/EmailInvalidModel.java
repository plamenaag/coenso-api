package com.primeholding.coenso.model;

public class EmailInvalidModel {
    private String message;
    private Boolean flag;

    public EmailInvalidModel(String message, Boolean flag) {
        this.message = message;
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}

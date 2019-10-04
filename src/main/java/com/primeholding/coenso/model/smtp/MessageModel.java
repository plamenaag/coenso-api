package com.primeholding.coenso.model.smtp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageModel {
    private String message;

    public MessageModel(String message) {
        this.message = message;
    }
}

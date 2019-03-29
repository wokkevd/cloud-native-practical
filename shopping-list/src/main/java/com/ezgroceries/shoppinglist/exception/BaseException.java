package com.ezgroceries.shoppinglist.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String[] params;

    BaseException(String message) {
        super(message);
        this.params = new String[0];
    }

    BaseException(String message, String... params) {
        super(message);
        this.params = params;
    }

    @Override
    public String getMessage() {
        return String.format(super.getMessage(), params);
    }
}

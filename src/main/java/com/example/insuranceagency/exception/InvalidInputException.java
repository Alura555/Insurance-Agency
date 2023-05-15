package com.example.insuranceagency.exception;

public class InvalidInputException extends RuntimeException {

    private String field;
    public InvalidInputException(String field, String message) {
        super(message);
        this.field = field;

    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

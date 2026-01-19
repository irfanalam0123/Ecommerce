package com.ecommerce.project.exceptions;

public class APIException extends RuntimeException {

    private String serialVersionUID;


    public APIException() {

    }

    public APIException(String message) {
        super(message);

    }
}

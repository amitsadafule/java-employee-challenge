package com.example.rqchallenge.exception;

public class InvalidApiResponse extends RuntimeException {

    public InvalidApiResponse(String message) {
        super(message);
    }
}

package com.logicea.cards.exceptions;

/**
 * Custom exception for email already exist
 */
public class EmailAlreadyExist extends RuntimeException {
    public EmailAlreadyExist(String message) {
        super(message);
    }
}
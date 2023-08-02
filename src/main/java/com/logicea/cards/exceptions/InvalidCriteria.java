package com.logicea.cards.exceptions;

/**
 * Custom exception for criteria search
 */
public class InvalidCriteria extends RuntimeException{
    public InvalidCriteria(String message) {
        super(message);
    }
}
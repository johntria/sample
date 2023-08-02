package com.logicea.cards.exceptions;

/**
 * Custom exception for card not found
 */
public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String message) {
        super(message);
    }
}
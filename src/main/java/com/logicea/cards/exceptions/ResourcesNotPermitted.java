package com.logicea.cards.exceptions;

/**
 * Custom exception for not allowed access in resources
 */
public class ResourcesNotPermitted extends RuntimeException {
    public ResourcesNotPermitted(String message) {
        super(message);
    }
}
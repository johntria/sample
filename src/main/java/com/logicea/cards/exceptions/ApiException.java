package com.logicea.cards.exceptions;

/**
 * Bean which wrap all exception to the application
 * @param errorTitle title of the error
 * @param error detail message of the error
 */
public record ApiException(String errorTitle,String error) {
}
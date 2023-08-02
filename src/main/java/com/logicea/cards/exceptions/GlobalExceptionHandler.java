package com.logicea.cards.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

/**
 * Global exception handler class responsible for handling exceptions thrown by the application's REST controllers.
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException that occurs when request validation fails.
     *
     * @param ex The MethodArgumentNotValidException object containing validation errors.
     * @return A ResponseEntity containing an ApiException with details about the validation errors.
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiException>  handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringJoiner stringJoiner = new StringJoiner(",");
        ex.getBindingResult().getAllErrors().forEach(error -> stringJoiner.add(error.getDefaultMessage()));
        ApiException exception = new ApiException("Error in fields", stringJoiner.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    /**
     * Handles EmailAlreadyExist exception that occurs when an email already exists during user registration.
     *
     * @param ex The EmailAlreadyExist object containing the exception message.
     * @return A ResponseEntity containing an ApiException with details about the email conflict error.
     */
    @ExceptionHandler(EmailAlreadyExist.class)
    public ResponseEntity<ApiException>  handleEmailAlreadyExist(EmailAlreadyExist ex) {
        ApiException exception = new ApiException("Email error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
    }

    /**
     * Handles UsernameNotFoundException that occurs when a user's username is not found during authentication.
     *
     * @param ex The UsernameNotFoundException object containing the exception message.
     * @return A ResponseEntity containing an ApiException with details about the credentials not found error.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiException> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiException exception = new ApiException("Credentials error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ApiException>  handleCardNotFound(CardNotFoundException ex) {
        ApiException exception = new ApiException("Card error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(ResourcesNotPermitted.class)
    public ResponseEntity<ApiException>  handleResourcesNotPermitted(ResourcesNotPermitted ex) {
        ApiException exception = new ApiException("Authorization error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception);
    }

    @ExceptionHandler(InvalidCriteria.class)
    public ResponseEntity<ApiException>  handleResourcesNotPermitted(InvalidCriteria ex) {
        ApiException exception = new ApiException("Invalid criteria", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    /**
     * Handles generic Exception that may occur during application processing.
     *
     * @param ex The Exception object containing the exception message.
     * @return A ResponseEntity containing an ApiException with details about the generic error.
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity handleGenericException(Exception ex) {
//        ApiException exception = new ApiException("Generic Error", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
//    }








}
package com.logicea.cards.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logicea.cards.requests.AuthenticationDTO;
import com.logicea.cards.requests.RegisterUserDTO;
import com.logicea.cards.responses.AuthResp;
import com.logicea.cards.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class responsible for handling authentication-related API endpoints that are publicly accessible.
 */
@RestController
@RequestMapping("/api/open/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authService;

    /**
     * Handles the registration endpoint for user registration.
     *
     * @param request The RegisterReq object containing user registration data.
     * @return A ResponseEntity containing the authentication response (AuthResp) with registration details.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResp> register(@Valid @RequestBody RegisterUserDTO request) {
        log.info("Started Rest request from AuthController#register. body {} ", request);
        AuthResp register = authService.register(request);
        log.info("Ended Rest request from AuthController#register. body {} ", register);
        return ResponseEntity.ok(register);
    }

    /**
     * Handles the authentication endpoint  for user authentication.
     *
     * @param request The AuthenticationReq object containing user authentication data.
     * @return A ResponseEntity containing the authentication response (AuthResp) with authentication details.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResp> authenticate(@Valid @RequestBody AuthenticationDTO request) {
        log.info("Started Rest request from AuthController#authenticate. body {} ", request);
        AuthResp authenticate = authService.authenticate(request);
        log.info("Ended Rest request from AuthController#authenticate. body {} ", request);
        return ResponseEntity.ok(authenticate);
    }
}
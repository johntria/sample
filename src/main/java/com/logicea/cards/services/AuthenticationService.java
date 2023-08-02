package com.logicea.cards.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.logicea.cards.exceptions.EmailAlreadyExist;
import com.logicea.cards.models.User;
import com.logicea.cards.repositories.UserRepository;
import com.logicea.cards.requests.AuthenticationDTO;
import com.logicea.cards.requests.RegisterUserDTO;
import com.logicea.cards.responses.AuthResp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The AuthenticationService  is responsible for user registration and authentication.
 * It provides methods to register new users and authenticate existing users using JWT-based authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The RegisterReq object containing user registration information.
     * @return An AuthResp object containing the JWT token generated for the registered user.
     * @throws EmailAlreadyExist If the email provided in the registration request already exists in the database.
     */
    public AuthResp register(RegisterUserDTO request) throws EmailAlreadyExist {
        var user = User
                .builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        if (userRepository.emailExists(request.getEmail())) {
            throw new EmailAlreadyExist("Email already exist");
        }
        userRepository.save(user);
        return new AuthResp(jwtService.generateToken(user));
    }

    /**
     * Authenticates an existing user based on the provided authentication request.
     *
     * @param request The AuthenticationReq object containing user authentication information.
     * @return An AuthResp object containing the JWT token generated for the authenticated user.
     * @throws UsernameNotFoundException If the provided email does not exist in the database.
     *                                   This indicates that the user with the given email is not registered.
     *                                   Invalid credentials will lead to this exception.
     */
    public AuthResp authenticate(AuthenticationDTO request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        return new AuthResp(jwtService.generateToken(user));
    }
}
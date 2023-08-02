package com.logicea.cards.configs;

import com.logicea.cards.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for application beans and security-related components.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    private final UserRepository userRepository;

    /**
     * Creates a {@link UserDetailsService } bean responsible for loading user-specific data.
     *
     * @return A  {@link UserDetailsService } implementation that retrieves user information from the UserRepository based on email.
     * @throws UsernameNotFoundException If the provided username (email) does not match any user in the UserRepository.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
    }

    /**
     * Creates an {@link AuthenticationProvider} bean responsible for user authentication.
     *
     * @return An {@link AuthenticationProvider} instance that uses the custom UserDetailsService to retrieve user details
     * and a BCryptPasswordEncoder for password encoding.
     */

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Creates an {@link AuthenticationManager} bean responsible for handling authentication requests.
     *
     * @param config An instance of {@link AuthenticationConfiguration } used to obtain the AuthenticationManager.
     * @return An AuthenticationManager instance used for authenticating users.
     * @throws Exception If there's an error creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a PasswordEncoder bean responsible for encoding user passwords using BCrypt algorithm.
     *
     * @return A {@link BCryptPasswordEncoder} instance for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
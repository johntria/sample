package com.logicea.cards.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for setting up security and authentication for the application.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Creates a SecurityFilterChain bean responsible for configuring the security settings for specific HTTP requests.
     *
     * @param httpSecurity The HttpSecurity object used for configuring security settings.
     * @return A SecurityFilterChain instance containing the configured security settings for the application.
     * @throws Exception If there is an error during the configuration process.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity
                .securityMatcher("/api/private/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * Configures shared security settings for the application.
     *
     * @param httpSecurity The HttpSecurity object used for configuring shared security settings.
     * @throws Exception If there is an error during the configuration process.
     */
    private void sharedSecurityConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }
}
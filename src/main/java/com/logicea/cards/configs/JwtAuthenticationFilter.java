package com.logicea.cards.configs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicea.cards.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter class responsible for handling JWT-based authentication for incoming HTTP requests.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;


    /**
     * Filters the incoming HTTP requests to check for JWT-based authentication.
     *
     * @param request     The incoming HttpServletRequest object.
     * @param response    The HttpServletResponse object for the outgoing response.
     * @param filterChain The filter chain to continue processing the request.
     * @throws ServletException If there is a servlet-related issue during request processing.
     * @throws IOException      If there is an I/O related issue during request processing.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractEmail(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(userDetails, jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            //todo:wrap it in external
        } catch (Exception e) {
            log.warn("Exception in JwtAuthenticationFilter#doFilterInternal due to {} ",e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("errorTitle", "Error with token");
            jsonResponse.put("error", "Your token is not valid, regenerate token");
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), jsonResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }


}
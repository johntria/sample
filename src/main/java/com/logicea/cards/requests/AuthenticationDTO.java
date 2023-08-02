package com.logicea.cards.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"password"})
public class AuthenticationDTO {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Pass is required")
    private String password;
}
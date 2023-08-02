package com.logicea.cards.requests;

import com.logicea.cards.models.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = {"password"})
public class RegisterUserDTO {
    @NotBlank(message = "Firstname is required.")
    private String firstname;
    @NotBlank(message = "Lastname is required.")
    private String lastname;
    @NotBlank(message = "Email address is required.")
    @Email(message = "The email address is invalid.", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;
    @NotBlank(message = "Password is required.")
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
}
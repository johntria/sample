package com.logicea.cards.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateCardDTO {
    @NotBlank(message = "Name card is required")
    private String name;
    private String description;
    @Pattern(regexp = "^#[A-Fa-f0-9]{6}$", message = "Color should be 6 alphanumeric characters prefixed with #")
    private String color;
}
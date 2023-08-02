package com.logicea.cards.requests;

import com.logicea.cards.models.Status;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateCardDTO {
    @NotNull(message = "Card id is required")
    private Integer cardId;

    /**
     * Name of card
     */
    @NotBlank(message = "Name card is required ")
    private String name;
    /**
     * Description of card
     */
    @NotNull(message = "Description of card is required")
    private String description;
    /**
     * Color of card
     */
    @Pattern(regexp = "^(#(?:[0-9a-fA-F]{6})|)$", message = "Color should be 6 alphanumeric characters prefixed with # or empty ")
    @NotNull(message = "Color of card is required ")
    private String color;

    @Enumerated
    @NotNull(message = "Status of card is required ")
    Status status;
}
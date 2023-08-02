package com.logicea.cards.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a card in application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_card")
public class Card {
    /**
     * Primary key of entity
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    /**
     * Name of card
     */
    @NotBlank(message = "Name of the card cannot be blank ")
    @Column(name = "name")
    private String name;
    /**
     * Description of card
     */
    @Column(name = "description")
    private String description;
    /**
     * Color of card
     */
    @Column(name = "color")
    private String color;
    /**
     * Creation date of card
     */
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;
    /**
     * Card owner
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    /**
     * Status of card
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


}
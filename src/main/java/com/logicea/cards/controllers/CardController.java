package com.logicea.cards.controllers;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logicea.cards.models.Card;
import com.logicea.cards.requests.CreateCardDTO;
import com.logicea.cards.requests.SearchCardCriteriaDTO;
import com.logicea.cards.requests.UpdateCardDTO;
import com.logicea.cards.services.CardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * REST Controller handler for managing cards.
 * This controller provides endpoints for creating, reading, updating, and deleting cards,
 * as well as searching for cards based on specified criteria.
 */
@RestController
@RequestMapping("/api/private/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {
    private final CardService cardService;

    /**
     * Endpoint for creating a new card with the provided information.
     *
     * @param dto The DTO (Data Transfer Object) containing the information to create the card.
     * @param principal The Principal object representing the authenticated user.
     * @return A ResponseEntity with the created card and HTTP status code 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CreateCardDTO dto, Principal principal) {
        log.info("Started Rest request from CardController#createCard. body {} ", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(dto, principal.getName()));
    }
    /**
     * Endpoint for retrieving a card with the specified cardId.
     *
     * @param cardId The unique identifier of the card to retrieve.
     * @param principal The Principal object representing the authenticated user.
     * @return A ResponseEntity with the retrieved card and HTTP status code 200 (OK) if the card is found.
     */
    @GetMapping(path = "{cardId}")
    public ResponseEntity<Card> retrieveCard(@PathVariable("cardId") Integer cardId, Principal principal) {
        log.info("Started Rest request from CardController#createCard. vars {} ", cardId);
        return ResponseEntity.ok(cardService.readCard(cardId, principal.getName()));
    }
    /**
     * Endpoint for updating an existing card with the provided information.
     *
     * @param dto The DTO containing the updated information for the card.
     * @param principal The Principal object representing the authenticated user.
     * @return A ResponseEntity with the updated card and HTTP status code 200 (OK) if the card is updated successfully.
     */
    @PutMapping
    private ResponseEntity<Card> updateCard(@Valid @RequestBody UpdateCardDTO dto, Principal principal) {
        log.info("Started Rest request from CardController#updateCard. body {} ", dto);
        return ResponseEntity.ok(cardService.updateCard(dto, principal.getName()));
    }
    /**
     * Endpoint for deleting a card with the specified cardId.
     *
     * @param cardId The unique identifier of the card to delete.
     * @param principal The Principal object representing the authenticated user.
     */
    @DeleteMapping(path = "{cardId}")
    private void deleteCard(@PathVariable("cardId") Integer cardId, Principal principal) {
        log.info("Started Rest request from CardController#deleteCard. vars {} ", cardId);
        cardService.deleteCard(cardId, principal.getName());
    }
    /**
     * Endpoint for searching for cards based on the criteria specified in the SearchCardCriteriaDTO.
     *
     * @param dto The DTO containing the search criteria for cards.
     * @param principal The Principal object representing the authenticated user.
     * @return A ResponseEntity with a Page of cards that match the specified search criteria and HTTP status code 200 (OK).
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Card>> searchCards(@RequestBody SearchCardCriteriaDTO dto, Principal principal) {
        log.info("Started Rest request from CardController#searchCards. vars {} ", dto);
        return ResponseEntity.ok().body(cardService.searchCards(dto, principal.getName()));
    }
}
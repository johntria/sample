package com.logicea.cards.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logicea.cards.models.Card;

/**
 * Repository interface for accessing and managing cards in the database.
 * This interface extends JpaRepository to provide basic CRUD (Create, Read, Update, Delete) operations for the Card entity.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    /**
     * Checks if a card with the specified cardId belongs to the user with the given email.
     *
     * @param cardId The unique identifier of the card to check.
     * @param email  The email of the user to whom the card ownership is being checked.
     * @return true if the card with the given cardId belongs to the user with the specified email,false otherwise.
     */
    @Query(nativeQuery = true, value = """
            SELECT IF(COUNT(C.ID) > 0,	'TRUE',	'FALSE')
             FROM cards.`_user` AS U
             INNER JOIN cards.`_card` C ON U.ID = C.USER_ID
             WHERE U.EMAIL = :email AND C.ID = :cardId
            """)
    Boolean isCardIdBelongToThisEmail(@Param("cardId") Integer cardId, @Param("email") String email);

    /**
     * Searches for cards based on the specified search criteria.
     *
     * @param userId       The ID of the user to whom the cards belong. If null, will not search by userId.
     * @param cardName         The name of the cards to search for. If null, will not search by name.
     * @param color        The color of the cards to search for. If null, will not search by color.
     * @param status       The status of the cards to search for. If null, will not search by status.
     * @param creationDate The minimum creation date of the cards to search for. If null, will not search by creationDate.
     * @param pageable     The pageable object specifying the pagination and sorting criteria for the search results.
     * @return A Page containing the cards that match the specified search criteria.
     */
    @Query(value = """
            SELECT * FROM cards.`_card` c 
            WHERE (:userId IS NULL OR c.user_id = :userId)
            AND (:cardName IS NULL OR c.name LIKE %:cardName%) 
            AND (:color IS NULL OR c.color LIKE %:color%) 
            AND (:status IS NULL OR c.status LIKE %:status%) 
            AND (:creationDate IS NULL OR c.creation_date= :creationDate)""",
            nativeQuery = true)
    Page<Card> searchCards(
            @Param("userId") Integer userId,
            @Param("cardName") String cardName,
            @Param("color") String color,
            @Param("status") String status,
            @Param("creationDate") LocalDateTime creationDate,
            Pageable pageable);

}
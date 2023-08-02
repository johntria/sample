package com.logicea.cards.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logicea.cards.models.Role;
import com.logicea.cards.models.User;
/**
 * Repository interface for managing User entities in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the User object if found, or an empty Optional if not found.
     */
    Optional<User> findByEmail(String email);
    /**
     * Find the role of a user by their email address.
     *
     * @param email The email address of the user whose role needs to be found.
     * @return An Optional containing the Role object if found, or an empty Optional if not found.
     */
    @Query(nativeQuery = true, value = "SELECT `role` FROM cards.`_user` WHERE email = :email ")
    Optional<Role> findRoleByEmail(@Param("email") String email);
    /**
     * Check if an email address exists in the database.
     *
     * @param email The email address to check for existence.
     * @return true if the email exists, false otherwise.
     */
    @Query(nativeQuery = true, value = "SELECT if(count(email) > 0, 'true', 'false') FROM cards.`_user` WHERE email = :email")
    Boolean emailExists(@Param("email") String email);
    /**
     * Find the user ID of a user by their email address.
     *
     * @param email The email address of the user whose ID needs to be found.
     * @return An Optional containing the user ID if found, or an empty Optional if not found.
     */
    @Query(nativeQuery = true, value = "SELECT `id` FROM cards.`_user` WHERE email = :email ")
    Optional<Integer> findUserIdByEmail(String email);
}
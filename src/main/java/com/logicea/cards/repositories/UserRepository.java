package com.logicea.cards.repositories;

import com.logicea.cards.models.Role;
import com.logicea.cards.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Query(nativeQuery = true, value = "SELECT `role` FROM cards.`_user` WHERE email = :email ")
    Optional<Role> findRoleByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "SELECT if(count(email) > 0, 'true', 'false') FROM cards.`_user` WHERE email = :email")
    Boolean emailExists(@Param("email") String email);
    @Query(nativeQuery = true, value = "SELECT `id` FROM cards.`_user` WHERE email = :email ")
    Optional<Integer> findUserIdByEmail(String email);
}
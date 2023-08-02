package com.logicea.cards.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity class representing a user in the application. Implements the UserDetails interface for Spring Security integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_user")
public class User implements UserDetails {
    /**
     * Primary key of entity
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    /**
     * Firstname of user
     */
    @NotEmpty(message = "The firstname is required.")
    @Column(name = "firstname")
    private String firstname;
    /**
     * Lastname of user
     */
    @NotEmpty(message = "The lastname is required.")
    @Column(name = "lastname")
    private String lastname;
    /**
     * Email of user & unique in order to identify
     */
    @Column(unique = true,name = "email")
    @NotEmpty(message = "The email is required.")
    private String email;
    /**
     * Password of user
     */
    @JsonIgnore
    @NotEmpty(message = "The password is required.")
    @Column(name = "password")
    private String password;
    /**
     * Role of user
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    /**
     * Returns the user's authorities, which represent the roles associated with the user.
     *
     * @return A collection of SimpleGrantedAuthority objects representing the user's role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the username of the user, which in this case is the user's email address.
     *
     * @return The email address of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Indicates whether the user's account has not expired (always true in this implementation).
     *
     * @return True, indicating the user's account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is not locked (always true in this implementation).
     *
     * @return True, indicating the user's account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials are not expired (always true in this implementation).
     *
     * @return True, indicating the user's credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled (always true in this implementation).
     *
     * @return True, indicating the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
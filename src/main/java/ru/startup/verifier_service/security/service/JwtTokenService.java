package ru.startup.verifier_service.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.startup.verifier_service.security.exception.InvalidTokenException;
import ru.startup.verifier_service.security.model.JwtResponse;
import ru.startup.verifier_service.security.model.Role;

/**
 * Service interface for JWT token management.
 * <p>
 * This interface defines methods for creating, refreshing, and validating JWT tokens,
 * as well as extracting user information from tokens.
 * </p>
 *
 * @since 1.0
 */
public interface JwtTokenService {

    String createAccessToken(String email);

    String createRefreshToken(String email);

    /**
     * Refreshes the user's access and refresh tokens.
     *
     * @param refreshToken The refresh token to use for token refreshing.
     * @return The updated JWT response containing the new access and refresh tokens.
     */
    JwtResponse refreshUserToken(String refreshToken);

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the roles from the given JWT token.
     *
     * @param token The JWT token from which to extract the roles.
     * @return The extracted roles.
     * @throws InvalidTokenException If the token is invalid or does not contain a roles claim.
     */
    String extractEmail(String token);
}


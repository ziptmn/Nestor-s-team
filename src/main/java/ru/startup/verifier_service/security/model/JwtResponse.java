package ru.startup.verifier_service.security.model;

/**
 * Represents a JWT response.
 * <p>
 * This record encapsulates the email information along with access and refresh tokens.
 * </p>
 *
 * @since 1.0
 */
public record JwtResponse(String login,
                          String accessToken,
                          String refreshToken) {
}

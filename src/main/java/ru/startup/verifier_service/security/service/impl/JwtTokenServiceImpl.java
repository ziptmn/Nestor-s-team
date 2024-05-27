package ru.startup.verifier_service.security.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.startup.verifier_service.security.exception.InvalidTokenException;
import ru.startup.verifier_service.security.model.JwtProperties;
import ru.startup.verifier_service.security.model.JwtResponse;
import ru.startup.verifier_service.security.model.Role;
import ru.startup.verifier_service.security.service.JwtTokenService;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * Implementation of the {@link JwtTokenService} interface for managing JWT tokens.
 * <p>
 * This class provides methods for creating access and refresh tokens, refreshing user tokens,
 * validating tokens, and extracting user information from tokens.
 * </p>
 *
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    /**
     * Properties related to JWT configuration.
     */
    private final JwtProperties jwtProperties;

    /**
     * Secret key used for JWT signing and verification.
     */
    private SecretKey key;

    /**
     * Initializes the secret key used for JWT operations.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String createAccessToken(String email) {
        return generateToken(email, jwtProperties.getAccess());
    }

    @Override
    public String createRefreshToken(String email) {
        return generateToken(email, jwtProperties.getRefresh());
    }

    @Override
    public JwtResponse refreshUserToken(String refreshToken) {
        try {
            if (validateToken(refreshToken)) {
                String email = extractEmail(refreshToken);
                String accessToken = generateToken(email, jwtProperties.getAccess());
                String newRefreshToken = generateToken(email, jwtProperties.getRefresh());

                return new JwtResponse(email, accessToken, newRefreshToken);
            } else throw new IllegalArgumentException();
        } catch (ExpiredJwtException | UnsupportedJwtException
                 | MalformedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException | UnsupportedJwtException
                 | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String extractEmail(String token) {
        Optional<Claims> claims = extractAllClaims(token);
        if (claims.isEmpty() || claims.get().getSubject() == null) {
            throw new InvalidTokenException("Invalid token.");
        }
        return claims.get().getSubject();
    }

    /**
     * Generates a JWT token based on the provided claims and expiration time.
     *
     * @param username       The subject of the token.
     * @param expirationTime The expiration time of the token.
     * @return The generated JWT token.
     */
    private String generateToken(String username, long expirationTime) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return The optional containing all extracted claims.
     */
    private Optional<Claims> extractAllClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return Optional.ofNullable(claimsJws.getBody());
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Malformed JWT token." + e.getMessage());
        }
    }
}

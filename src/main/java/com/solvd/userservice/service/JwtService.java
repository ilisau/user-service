package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import io.jsonwebtoken.Claims;

public interface JwtService {

    /**
     * Parse token.
     *
     * @param token token
     * @return claims
     */
    Claims parse(String token);

    /**
     * Generate token.
     *
     * @param type type
     * @param user user
     * @return token
     */
    String generateToken(JwtTokenType type, User user);

    /**
     * Check if token is of type.
     *
     * @param token token
     * @param type  type
     * @return true if token is of type
     */
    boolean isTokenType(String token, JwtTokenType type);

    /**
     * Validate token.
     *
     * @param token token
     * @return true if token is valid
     */
    boolean validateToken(String token);

    /**
     * Retrieve user id from token.
     *
     * @param token token
     * @return user id
     */
    String retrieveUserId(String token);

}

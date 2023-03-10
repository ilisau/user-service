package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import io.jsonwebtoken.Claims;

public interface JwtService {

    Claims parse(String token);

    String generateToken(JwtTokenType type, User user);

    boolean isTokenType(String token, JwtTokenType type);

    boolean validateToken(String token);

    Long retrieveUserId(String token);

}

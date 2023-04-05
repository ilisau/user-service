package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.service.property.JwtProperties;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    private void postConstruct() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String generateToken(JwtTokenType type, User user) {
        return switch (type) {
            case ACCESS -> generateAccessToken(user);
            case REFRESH -> generateRefreshToken(user);
            case ACTIVATION -> generateActivationToken(user);
            case RESET -> generateResetToken(user);
        };
    }

    @Override
    public boolean isTokenType(String token, JwtTokenType type) {
        Claims claims = parse(token);
        return Objects.equals(claims.get("type"), type.name());
    }

    private String generateRefreshToken(User user) {
        final Instant refreshExpiration = Instant.now().plus(jwtProperties.getRefresh(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("type", JwtTokenType.REFRESH.name())
                .setExpiration(Date.from(refreshExpiration))
                .signWith(key)
                .compact();
    }

    private String generateAccessToken(User user) {
        final Instant accessExpiration = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("type", JwtTokenType.ACCESS.name())
                .claim("role", user.getRole())
                .setExpiration(Date.from(accessExpiration))
                .signWith(key)
                .compact();
    }

    private String generateActivationToken(User user) {
        final Instant accessExpiration = Instant.now().plus(jwtProperties.getActivation(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("type", JwtTokenType.ACTIVATION.name())
                .setExpiration(Date.from(accessExpiration))
                .signWith(key)
                .compact();
    }

    private String generateResetToken(User user) {
        final Instant accessExpiration = Instant.now().plus(jwtProperties.getReset(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("type", JwtTokenType.RESET.name())
                .setExpiration(Date.from(accessExpiration))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String retrieveUserId(String token) {
        return parse(token)
                .get("id")
                .toString();
    }

}

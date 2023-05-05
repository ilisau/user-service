package com.solvd.userservice.service.impl.generator;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.service.property.JwtProperties;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenGenerator implements TokenGenerator {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    private void postConstruct() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public String generate(final User user) {
        final Instant refreshExpiration = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("type", JwtTokenType.REFRESH.name())
                .setExpiration(Date.from(refreshExpiration))
                .signWith(key)
                .compact();
    }

}

package com.solvd.userservice.service.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private final long access;
    private final long refresh;
    private final long activation;
    private final long reset;
    private final String secret;

}
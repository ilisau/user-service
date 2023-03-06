package com.solvd.userservice.service.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final Properties properties;

}

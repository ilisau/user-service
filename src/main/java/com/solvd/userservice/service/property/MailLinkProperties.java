package com.solvd.userservice.service.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "mail.links")
public class MailLinkProperties {

    private final String activation;

    private final String restore;

}

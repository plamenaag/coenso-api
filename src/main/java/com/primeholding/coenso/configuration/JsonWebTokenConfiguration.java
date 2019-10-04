package com.primeholding.coenso.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonWebTokenConfiguration {

    @Value("${authentication.entry-point}")
    private String entryPoint;

    @Value("${authorization.header}")
    private String header;

    @Value("${authorization.type}")
    private String type;

    @Value("${authorization.secret}")
    private String secret;

    @Value("${authorization.expiration}")
    private Long expiration;

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getHeader() {
        return header;
    }

    public String getType() {
        return type;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }
}

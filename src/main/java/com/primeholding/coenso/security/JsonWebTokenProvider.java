package com.primeholding.coenso.security;

import com.primeholding.coenso.configuration.JsonWebTokenConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JsonWebTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenProvider.class);

    private JsonWebTokenConfiguration configuration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(configuration.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Authentication authentication) {
        InternalAccount principal = (InternalAccount) authentication.getPrincipal();
        Long timeToLive = configuration.getExpiration();
        if (timeToLive == null) {
            LOGGER.error("JWT expiration not set");
            return null;
        }

        Date createdAt = new Date();
        Date expirationDate = new Date(createdAt.getTime() + timeToLive);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(createdAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,
                        configuration.getSecret())
                .compact();
    }

    public boolean isTokenValid(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(configuration.getSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOGGER.warn("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOGGER.warn("Invalid JWT");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            LOGGER.warn("Expired JWT");
        } catch (UnsupportedJwtException ex) {
            LOGGER.warn("Unsupported JWT");
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("JWT claims string is empty.");
        }

        return false;
    }

    @Autowired
    public void setConfiguration(JsonWebTokenConfiguration configuration) {
        this.configuration = configuration;
    }
}

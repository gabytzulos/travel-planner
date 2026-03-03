package com.gabrielluciu.travelplanner.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "jwt")
@Setter
public class JwtService {

    private String secret;

    private String issuer;

    @Getter
    private long expirationMs;

    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UUID userId, List<String> roles, Map<String, Object> extraClaims) {
        if (userId == null) {
            throw new IllegalArgumentException("Cannot generate JWT: userId is null");
        }

        Instant now = Instant.now();

        JwtBuilder builder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(this.issuer)
                .claim(JwtClaims.ROLES, roles)
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(this.expirationMs)))
                .signWith(this.signingKey);

        this.addClaims(builder, extraClaims);

        return builder.compact();
    }

    private void addClaims(JwtBuilder builder, Map<String, Object> extraClaims) {
        if (extraClaims == null) {
            return;
        }

        extraClaims.forEach((key, value) -> {
            if (JwtClaims.RESERVED.contains(key)) {
                return;
            }

            builder.claim(key, value);
        });
    }

}

package com.encora.taskmanager.jwt;

import com.encora.taskmanager.constant.StringConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.time.Instant;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private static final long JWT_TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(StringConstants.JWT_VALIDITY);

    public String generateToken(final UserDetails userDetails) {
        final Map<String, String> claims = new HashMap<>();
        claims.put("iss", StringConstants.JWT_ISSUER);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(JWT_TOKEN_VALIDITY)))
                .signWith(generateSecretKey())
                .compact();
    }

    private SecretKey generateSecretKey() {
        final byte[] bytes = Base64.getDecoder().decode(StringConstants.JWT_SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractUserName(final String jwtToken) {
        final Claims claims = getClaims(jwtToken);
        return claims.getSubject();
    }

    public boolean isTokenExpired(final String jwtToken) {
        final Claims claims = getClaims(jwtToken);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    private Claims getClaims(final String jwtToken) {
        return Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}

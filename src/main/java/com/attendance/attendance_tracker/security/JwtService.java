package com.attendance.attendance_tracker.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.attendance.attendance_tracker.entity.Role;
import com.attendance.attendance_tracker.entity.Teacher;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Issues and validates HS256-signed JWTs. The signing key is loaded once at startup
 * and must be at least 32 bytes; the application will fail to start otherwise.
 */
@Service
@Slf4j
public class JwtService {

    private final String secret;
    private final String issuer;
    private final long ttlSeconds;
    private SecretKey signingKey;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.ttl-seconds}") long ttlSeconds) {
        this.secret = secret;
        this.issuer = issuer;
        this.ttlSeconds = ttlSeconds;
    }

    @PostConstruct
    void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                "app.jwt.secret must be at least 32 bytes for HS256; got " + keyBytes.length);
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String issueToken(Teacher teacher) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlSeconds * 1000L);
        return Jwts.builder()
            .issuer(issuer)
            .subject(teacher.getEmail())
            .claim("uid", teacher.getId())
            .claim("role", teacher.getRole().name())
            .issuedAt(now)
            .expiration(exp)
            .signWith(signingKey, Jwts.SIG.HS256)
            .compact();
    }

    /**
     * Parses and validates the token. Throws {@link JwtException} for any failure
     * (expired, tampered, malformed, bad signature).
     */
    public Claims parse(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)
            .requireIssuer(issuer)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    public Role extractRole(Claims claims) {
        return Role.valueOf(claims.get("role", String.class));
    }
}

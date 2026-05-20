package com.campus.mentalhealth.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final Key KEY;

    static {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.isEmpty()) {
            secret = "campus-mental-health-jwt-secret-key-2026-stable-production-grade";
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            // Pad to minimum 256 bits for HS256
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
            keyBytes = padded;
        }
        KEY = Keys.hmacShaKeyFor(keyBytes);
    }
    private static final long EXPIRATION = 24 * 60 * 60 * 1000L; // 24 hours

    public static String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT token expired");
            return null;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.warn("JWT token malformed");
            return null;
        } catch (io.jsonwebtoken.SignatureException e) {
            log.warn("JWT signature verification failed");
            return null;
        } catch (Exception e) {
            log.warn("JWT parse failed: {}", e.getMessage());
            return null;
        }
    }

    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        if (claims == null) return null;
        return Long.parseLong(claims.getSubject());
    }

    public static String getRole(String token) {
        Claims claims = parseToken(token);
        if (claims == null) return null;
        return claims.get("role", String.class);
    }
}

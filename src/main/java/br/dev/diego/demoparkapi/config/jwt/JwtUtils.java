package br.dev.diego.demoparkapi.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    private final static String JWT_PREFIX = "Bearer ";
    private final static String JWT_SECRET = "demopark-api";
    private final static long EXPIRE_DAYS = 0;
    private final static long EXPIRE_HOURS = 0;
    private final static long EXPIRE_MINUTES = 2;

    public static JwtToken generateJwtToken(String username, String role) {
        Date issuedAt = new Date();
        Date expirationDate = generateExpirationDate(issuedAt);

        String token = Jwts.builder()
                .header().add("typ", "JWT")
                .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(generateKey())
                .claim("role", role)
                .compact();
        return new JwtToken(token);
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
        }
        return false;
    }

    private static Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refactorToken(token))
                    .getPayload();
        } catch (JwtException e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
        }
        return null;
    }

    private static String refactorToken(String token) {
        if (token.contains(JWT_PREFIX)) {
            return token.replace(JWT_PREFIX, "");
        }
        return token;
    }

    private static SecretKey generateKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private static Date generateExpirationDate(Date start) {
        LocalDateTime now = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expiration = now.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
    }

}

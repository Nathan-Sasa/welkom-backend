package com.nathdev.welkom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secreteKey}")
    private String jwtSecretKey;

    @Value("${jwt.expire}")
    private long jwtExpirationTime;

    @Value("${jwt.refreshExpire}")
    private long refreshExpirationTime;

    public String generateAccessToken(String username, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, email, email, jwtExpirationTime);
    }

    public String generateRefreshToken(String username) {
        return createToken(new HashMap<>(), username, null, refreshExpirationTime);
    }

    private String createToken(Map<String, Object> claims, String subject, String email, long expiration) {

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expiration);

        var builder = Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey());

        if (email != null) {
            builder.audience().add(email);
        }
        return builder.compact();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String getJwtFromCookies(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public ResponseCookie generateCookie(String name, String value, long maxAgeMillis) {
        return ResponseCookie.from(name, value)
                .path("/")
                .maxAge(maxAgeMillis / 1000)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie getCleanCookie(String name){
        return ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();
    }

    public Boolean validateToken(String token, String expectedUsername) {
        final String username = extractUsername(token);
        //        log.info("jwtToken validation result: {}", isValid);
        return username.equals(expectedUsername) && !isTokenExpire(token);
    }

    public boolean isTokenExpire(String token) {
        Date expiration = extractExpirationDate(token);
        return expiration.before(Date.from(Instant.now()));
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

package com.vikas.EZmanage.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JWTUtil {

    private static final String SECRET_KEY = "my-secure-and-long-secret-key-for-jwt-signing-minimum-32-bytes";
    private static final Key signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username, long expirationInMinutes) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateTokenAndGetUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch(JwtException e) {
            return null; // invalid token
        }
    }

    public String extractJWTTokenFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            return jwtToken.substring(7);
        }
        return null;
    }

    public String extractJWTRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }

        String refreshToken = null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("RefreshToken")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        return refreshToken;
    }

}

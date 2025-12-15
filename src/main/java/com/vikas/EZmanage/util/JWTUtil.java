package com.vikas.EZmanage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    private static final String SECRET_KEY = "my-secure-and-long-secret-key-for-jwt-signing-minimum-32-bytes";
    private static final Key signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username, long expirationInMinutes, Collection<? extends GrantedAuthority> authorities) {
        String roles = authorities == null || authorities.isEmpty() ? "" :
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
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
            return null;  // In case of invalid token
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

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String roles = claims.get("roles", String.class);

        if(roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }

        String[] rolesArray = roles.split(",");
        return java.util.Arrays.stream(rolesArray)
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }
}

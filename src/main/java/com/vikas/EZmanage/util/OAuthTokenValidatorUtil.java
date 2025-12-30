package com.vikas.EZmanage.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Component
public class OAuthTokenValidatorUtil {

    public String isTokenValid(String accessToken) {
        String issuer = getIssuerIdFromToken(accessToken);
        if(issuer == null || issuer.isBlank()) {
            return null;
        }
        JwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
        Jwt jwt = decoder.decode(accessToken);
        if(jwt != null) {
            return (String) jwt.getClaims().get("sub");
        }
        return null;
    }

    public static String getIssuerIdFromToken(String accessToken) {
        try {
            String[] tokenParts = accessToken.split("\\.");
            if (tokenParts.length < 2) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            String jsonPayload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(jsonPayload, Map.class);
            String issuer = (String) payloadMap.get("iss");
            if(issuer == null || issuer.isBlank()) {
                return null;
            }
            if (!"https://accounts.google.com".equals(issuer)) {
                throw new SecurityException("Untrusted Issuer");
            }
            return issuer;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

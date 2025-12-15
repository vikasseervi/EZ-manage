package com.vikas.EZmanage.security;

import com.vikas.EZmanage.token.JWTAuthenticationToken;
import com.vikas.EZmanage.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTRefreshFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public  JWTRefreshFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getServletPath().equals("/ezmanage/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = jwtUtil.extractJWTRefreshTokenFromRequest(request);
        if(refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(refreshToken);
        Authentication authResult = authenticationManager.authenticate(jwtAuthenticationToken);
        if(authResult.isAuthenticated()) {
            String newToken = jwtUtil.generateToken(authResult.getName(), 5, authResult.getAuthorities()); // 5 minutes expiry
            response.setHeader("Authorization", "Bearer " + newToken);
        }
    }
}

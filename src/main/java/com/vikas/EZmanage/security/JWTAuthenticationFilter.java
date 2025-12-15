package com.vikas.EZmanage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikas.EZmanage.dto.LoginRequestDTO;
import com.vikas.EZmanage.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getServletPath().equals("/ezmanage/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();  // for JSON parsing
        LoginRequestDTO loginRequestDTO = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);  // read login data

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        Authentication authResult = authenticationManager.authenticate(authToken);

        if(authResult.isAuthenticated()) {
            String token = jwtUtil.generateToken(authResult.getName(), 1, authResult.getAuthorities()); // 1 minutes expiry
            response.setHeader("Authorization", "Bearer " + token);

            System.out.println("*************" + authResult.getAuthorities() + "****************");

            String refreshToken = jwtUtil.generateToken(authResult.getName(), 5, authResult.getAuthorities()); // 5 days expiry

            Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setAttribute("SameSite", "Strict"); // to prevent CSRF
            refreshTokenCookie.setPath("/ezmanage/auth/refresh");
            refreshTokenCookie.setMaxAge(5 * 60); // 5 minutes in seconds
            response.addCookie(refreshTokenCookie);

            // Send response body to display token and username on client side(postman etc)
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"token\":\"" + token + "\",\"username\":\"" + authResult.getName() + "\"}");
            response.setContentType("application/json");
        }
    }

}

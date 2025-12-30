package com.vikas.EZmanage.security;

import com.vikas.EZmanage.util.OAuthTokenValidatorUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class OAuthValidationFilter extends OncePerRequestFilter {

    private final OAuthTokenValidatorUtil oAuthTokenValidatorUtil;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    public OAuthValidationFilter(OAuthTokenValidatorUtil oAuthTokenValidatorUtil, EmployeeUserDetailsService employeeUserDetailsService) {
        this.oAuthTokenValidatorUtil = oAuthTokenValidatorUtil;
        this.employeeUserDetailsService = employeeUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            String username = oAuthTokenValidatorUtil.isTokenValid(accessToken);

            if (username != null && !username.isBlank()) {
                EmployeeUserDetails userDetails = employeeUserDetailsService.loadUserByUsername(username);
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("Authenticated via OAuth: " + username);
            }
        }

        filterChain.doFilter(request, response);
    }

}

package com.vikas.EZmanage.authenticationProvider;

import com.vikas.EZmanage.security.EmployeeUserDetailsService;
import com.vikas.EZmanage.token.JWTAuthenticationToken;
import com.vikas.EZmanage.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JWTUtil jwtUtil;
    private EmployeeUserDetailsService employeeUserDetailsService;

    public  JWTAuthenticationProvider(JWTUtil jwtUtil, EmployeeUserDetailsService employeeUserDetailsService){
        this.employeeUserDetailsService = employeeUserDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JWTAuthenticationToken) authentication).getToken();
        String username = jwtUtil.validateTokenAndGetUsername(token);

        if (username == null) {
            throw new BadCredentialsException("Invalid JWT token");
        }

        List<GrantedAuthority> authorities = jwtUtil.getAuthoritiesFromToken(token);
        return new JWTAuthenticationToken(token, username, authorities);

//        EmployeeUserDetails employeeUserDetails = employeeUserDetailsService.loadUserByUsername(username);
//        return new UsernamePasswordAuthenticationToken(employeeUserDetails, null, employeeUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

package com.vikas.EZmanage.authenticationProvider;

import com.vikas.EZmanage.security.EmployeeUserDetails;
import com.vikas.EZmanage.security.EmployeeUserDetailsService;
import com.vikas.EZmanage.token.JWTAuthenticationToken;
import com.vikas.EZmanage.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
            throw new BadCredentialsException("Invalid or Expired JWT token");
        }

        EmployeeUserDetails employeeUserDetails = employeeUserDetailsService.loadUserByUsername(username);

        if(!employeeUserDetails.isEnabled()) {  // check if user is active/enabled by active flag
            throw new DisabledException("User account is disabled/inactive");
        }

        return new JWTAuthenticationToken(token, employeeUserDetails.getUsername(), employeeUserDetails.getAuthorities());

//        List<GrantedAuthority> authorities = jwtUtil.getAuthoritiesFromToken(token);
//        return new JWTAuthenticationToken(token, username, authorities);

        //        EmployeeUserDetails employeeUserDetails = employeeUserDetailsService.loadUserByUsername(username);
        //        return new UsernamePasswordAuthenticationToken(employeeUserDetails, null, employeeUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

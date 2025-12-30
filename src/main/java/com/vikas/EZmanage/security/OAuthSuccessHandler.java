package com.vikas.EZmanage.security;

import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.repository.EmployeeRepository;
import com.vikas.EZmanage.repository.RoleRepository;
import com.vikas.EZmanage.service.EmployeeService;
import com.vikas.EZmanage.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final EmployeeService employeeService;
    private final EmployeeUserDetailsService employeeUserDetailsService;
    private final JWTUtil jwtUtil;


    @Autowired
    public OAuthSuccessHandler(OAuth2AuthorizedClientService oAuth2AuthorizedClientService, OAuth2AuthorizedClientService oAuth2AuthorizedClientService1, EmployeeService employeeService, EmployeeUserDetailsService employeeUserDetailsService, JWTUtil jwtUtil) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService1;
        this.employeeService = employeeService;
        this.employeeUserDetailsService = employeeUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String email = authToken.getPrincipal().getAttribute("email");

        Employee employee = employeeService.registerOrFetchOAuthUser(
                email,
                authToken.getPrincipal().getAttribute("given_name"),
                authToken.getPrincipal().getAttribute("family_name")
        );

        EmployeeUserDetails userDetails = employeeUserDetailsService.loadUserByUsername(employee.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails.getUsername(), 15, userDetails.getAuthorities());
        String refreshToken = jwtUtil.generateToken(userDetails.getUsername(), 60 * 24 * 5, userDetails.getAuthorities());

        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Should be true in production (HTTPS)
        refreshTokenCookie.setPath("/ezmanage/auth/refresh");
        response.addCookie(refreshTokenCookie);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + accessToken + "\",\"username\":\"" + employee.getUsername() + "\"}");
    }

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
//        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
//                authToken.getAuthorizedClientRegistrationId(), authToken.getName());
//
//        if(client != null) {
//            String idToken = null;
//            if(authToken.getPrincipal() instanceof OidcUser) {
//                OidcUser oidcUser = (OidcUser) authToken.getPrincipal();
//                idToken = oidcUser.getIdToken().getTokenValue();
//            }
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write("{\"id_token\" : \""+ idToken +"\"}");
//            response.getWriter().flush();
//        }
//        else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization failed");
//        }
//    }
}

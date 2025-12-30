package com.vikas.EZmanage.config;

import com.vikas.EZmanage.authenticationProvider.JWTAuthenticationProvider;
import com.vikas.EZmanage.security.*;
import com.vikas.EZmanage.util.JWTUtil;
import com.vikas.EZmanage.util.OAuthTokenValidatorUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

//Spring pehle application context setup karta h, fir @Configuration classes run hote h, phir beans create hote h aur finally application chalna start hota h.
@Configuration
// iska main purpose security rules aur settings ko define karna aur unhe activate karna h, default security behavior override karta h.
@EnableWebSecurity
// ae method level pe security enable kar deta h, jisse hum @PreAuthorize jaise annotations use kar sakte h apne methods pe to enforce security constraints.
@EnableMethodSecurity
public class SecurityConfig {

    private JWTUtil jwtUtil;
    private EmployeeUserDetailsService employeeUserDetailsService;
    private OAuthTokenValidatorUtil oAuthTokenValidatorUtil;

    @Autowired
    public SecurityConfig(JWTUtil jwtUtil, EmployeeUserDetailsService employeeUserDetailsService, OAuthTokenValidatorUtil oAuthTokenValidatorUtil) {
        this.jwtUtil = jwtUtil;
        this.employeeUserDetailsService = employeeUserDetailsService;
        this.oAuthTokenValidatorUtil = oAuthTokenValidatorUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider bean jo database se user details fetch karne ke liye use hota h
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(employeeUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // JWTAuthenticationProvider bean jo JWT tokens ko validate karne ke liye use hota h
    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(jwtUtil, employeeUserDetailsService);
    }

    // AuthenticationManager bean jo authentication providers ko manage karta h
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(
                daoAuthenticationProvider(),
                jwtAuthenticationProvider()
        ));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, JWTUtil jwtUtil, OAuthSuccessHandler oAuthSuccessHandler) throws Exception {

        // Custom JWT authentication filter
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);
        // Custom JWT validation filter
        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(authenticationManager);
        // Custom JWT refresh filter
        JWTRefreshFilter jwtRefreshFilter = new JWTRefreshFilter(authenticationManager, jwtUtil);
        // Custom OAuth validation filter (finally bangaya)
        OAuthValidationFilter oAuthValidationFilter = new OAuthValidationFilter(oAuthTokenValidatorUtil, employeeUserDetailsService);

        http
                .csrf(csrfConfigurer -> csrfConfigurer.disable())  // ae humare application me CSRF protection ko disable kar deta h, but production me ise enable karna chahiye, especially for form based authentication.
                .cors(Customizer.withDefaults())  // ae Cross-Origin Resource Sharing (CORS) ko default settings ke sath enable kar deta h, jisse different origins se aane wale requests ko handle kiya ja sake.
                .authorizeHttpRequests(request -> request
                        // .requestMatchers("/ezmanage/auth/**").hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN")  // We can authorize here or in controller using @PreAuthorize
                        .requestMatchers("/ezmanage/auth/**", "/login").permitAll()
                        .anyRequest().authenticated()
                )
//                .httpBasic(Customizer.withDefaults())  // ae api access ke liye h, like postman
//                .formLogin(Customizer.withDefaults())  // ae form ke liye.. for browser
//                .formLogin(form -> form.disable())
//                .formLogin(form -> form
//                        .defaultSuccessUrl("/ezmanage/employees", true)  // ae login ke baad redirect karne ke liye default URL set kar deta h
//                        .permitAll()
//                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/ezmanage/employees", true)
                        .successHandler(oAuthSuccessHandler)
                )  // ae OAuth2 login ko default settings ke sath enable kar deta h, jisse users third-party providers jaise Google ya Facebook ke through authenticate kar sakte h
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                )
                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ae session ko hamesha create karega. for example, jab bhi user login karega. ae form based authentication ke liye useful h
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // ae tabhi session create karega jab zarurat ho. for example, agar koi feature session pe depend karta h toh hi session create karega. ae form based authentication ke liye default h, better than ALWAYS
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ae session ko bilkul bhi use nahi karega. for example, jab hum purely token based authentication use karte h toh like Basic Auth ya JWT
//                        .sessionCreationPolicy(SessionCreationPolicy.NEVER) // ae session create nahi karega, lekin agar koi existing session h toh use karega. for example, agar humare paas kuch legacy session based features h to
//                        .maximumSessions(1).maxSessionsPreventsLogin(true)  // ae ek user ke liye ek hi session allow karega
                )
                .addFilterBefore(oAuthValidationFilter, UsernamePasswordAuthenticationFilter.class)  // ae custom OAuth validation filter ko JWTAuthenticationFilter se pehle add kar deta h, jisse OAuth tokens ko validate kiya ja sake before standard authentication processing
                .addFilterAfter(jwtAuthenticationFilter, OAuthValidationFilter.class)  // ae custom JWT authentication filter ko UsernamePasswordAuthenticationFilter se pehle add kar deta h, jisse JWT tokens ko validate kiya ja sake before standard authentication processing
                .addFilterAfter(jwtRefreshFilter, JWTAuthenticationFilter.class)  // ae custom JWT refresh filter ko JWTAuthenticationFilter ke baad add kar deta h, jisse refresh token requests ko handle kiya ja sake (refresh should run before validation)
                .addFilterAfter(jwtValidationFilter, JWTRefreshFilter.class)  // ae custom JWT validation filter ko JWTRefreshFilter ke baad add kar deta h, jisse har request pe JWT token validate kiya ja sake
        ;
        return http.build();
    }

}

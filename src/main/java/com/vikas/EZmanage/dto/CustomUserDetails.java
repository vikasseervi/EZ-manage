package com.vikas.EZmanage.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, boolean active, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
    }

    public CustomUserDetails(CustomUserDetailsBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.active = builder.active;
        this.authorities = builder.authorities;
    }

    public static CustomUserDetailsBuilder builder() {
        return new CustomUserDetailsBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities( ) {
        return authorities;
    }

    @Override
    public String getPassword( ) {
        return password;
    }

    @Override
    public String getUsername( ) {
        return username;
    }

    @Override
    public boolean isEnabled( ) {
        return active;
    }

    @Override
    public boolean isAccountNonExpired( ) {
        return true;
    }

    @Override
    public boolean isAccountNonLocked( ) {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired( ) {
        return true;
    }

    public static class CustomUserDetailsBuilder {
        private String username;
        private String password;
        private boolean active;
        private Collection<? extends GrantedAuthority> authorities;

        public CustomUserDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CustomUserDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CustomUserDetailsBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public CustomUserDetailsBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public CustomUserDetails build() {
            return new CustomUserDetails(this);
        }

    }
}

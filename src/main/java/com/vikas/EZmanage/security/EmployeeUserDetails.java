package com.vikas.EZmanage.security;

import com.vikas.EZmanage.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class EmployeeUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public EmployeeUserDetails(String username, String password, boolean active, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
    }

    public EmployeeUserDetails(EmployeeUserDetailsBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.active = builder.active;
        this.authorities = builder.authorities;
    }

    public static EmployeeUserDetailsBuilder builder( ) {
        return new EmployeeUserDetailsBuilder();
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

    public static class EmployeeUserDetailsBuilder {
        private String username;
        private String password;
        private boolean active;
        private Collection<? extends GrantedAuthority> authorities;

        public EmployeeUserDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public EmployeeUserDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public EmployeeUserDetailsBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public EmployeeUserDetailsBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public EmployeeUserDetails build( ) {
            return new EmployeeUserDetails(this);
        }

    }
}

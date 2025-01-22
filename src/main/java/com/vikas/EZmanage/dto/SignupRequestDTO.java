package com.vikas.EZmanage.dto;

import com.vikas.EZmanage.entity.Role;
import java.util.Set;

public class SignupRequestDTO {
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private Set<Role> roles;

    public SignupRequestDTO( ) {}

    public SignupRequestDTO(String username, String password, String first_name, String last_name, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.roles = roles;
    }

    public String getUsername( ) {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword( ) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name( ) {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name( ) {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail( ) {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles( ) {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "SignupRequestDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
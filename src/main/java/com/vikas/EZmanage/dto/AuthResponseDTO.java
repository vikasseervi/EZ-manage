package com.vikas.EZmanage.dto;

// package com.vikas.EZmanage.dto;

import java.util.Set;

public class AuthResponseDTO {
    private String token;
    private String username;
    private Set<String> roles;

    public AuthResponseDTO(String token, String username, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public String getToken( ) {
        return token;
    }

    public String getUsername( ) {
        return username;
    }

    public Set<String> getRoles( ) {
        return roles;
    }
}

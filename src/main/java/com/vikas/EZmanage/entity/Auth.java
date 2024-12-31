package com.vikas.EZmanage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "auth")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 68)
    private String passwordHash;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Auth( ) {}

    public Auth(Long id, String username, String passwordHash, Boolean active) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.active = active;
    }

    public Long getId( ) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername( ) {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash( ) {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getActive( ) {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString( ) {
        return "Auth{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", active=" + active +
                '}';
    }
}

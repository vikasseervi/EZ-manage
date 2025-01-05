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

    private Auth(AuthBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.passwordHash = builder.passwordHash;
        this.active = builder.active;
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

    public static class AuthBuilder {
        private Long id;
        private String username;
        private String passwordHash;
        private Boolean active = true;

        public AuthBuilder() { }

        public AuthBuilder id(Long id){
            this.id = id;
            return this;
        }

        public AuthBuilder username(String username){
            this.username = username;
            return this;
        }

        public AuthBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public AuthBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Auth build() {
            return new Auth(this);
        }
    }
}

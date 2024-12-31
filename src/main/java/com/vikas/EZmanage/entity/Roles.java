package com.vikas.EZmanage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum RoleName {
        ROLE_EMPLOYEE, ROLE_MANAGER, ROLE_ADMIN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true)
    private RoleName roleName;

    public Roles( ) {}

    public Roles(Long id, RoleName roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Long getId( ) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRoleName( ) {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString( ) {
        return "Roles{" +
                "id=" + id +
                ", roleName=" + roleName +
                '}';
    }
}

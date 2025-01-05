package com.vikas.EZmanage.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,
        region = "roleCache")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum RoleName {
        ROLE_EMPLOYEE, ROLE_MANAGER, ROLE_ADMIN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true)
    private RoleName roleName;

    public Role( ) {}

    public Role(Long id, RoleName roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Role(RoleBuilder builder) {
        this.id = builder.id;
        this.roleName = builder.roleName;
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
        return "Role{" +
                "id=" + id +
                ", roleName=" + roleName +
                '}';
    }

    public static class RoleBuilder {
        private Long id;
        private RoleName roleName;

        public RoleBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public RoleBuilder roleName(RoleName roleName) {
            this.roleName = roleName;
            return this;
        }
        public Role build() {
            return new Role(this);
        }

    }
}

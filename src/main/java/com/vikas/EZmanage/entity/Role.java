package com.vikas.EZmanage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "roleCache")
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

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<Employee> employees;

    public Role() {}

    public Role(Long id, RoleName roleName, Set<Employee> employees) {
        this.id = id;
        this.roleName = roleName;
        this.employees = employees;
    }

    private Role(RoleBuilder builder) {
        this.id = builder.id;
        this.roleName = builder.roleName;
        this.employees = builder.employees;
    }

    // Helper method to add an employee
    public void addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getRoles().add(this);
    }

    // Helper method to remove an employee
    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getRoles().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if(employees != null && !employees.isEmpty()) {
            this.employees = employees;
            for(Employee employee : employees) {
                addEmployee(employee);
            }
        }
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName=" + roleName +
                ", employees=" + employees +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                roleName == role.roleName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }

    public static class RoleBuilder {
        private Long id;
        private RoleName roleName;
        private Set<Employee> employees;

        public RoleBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoleBuilder roleName(RoleName roleName) {
            this.roleName = roleName;
            return this;
        }

        public RoleBuilder employees(Set<Employee> employees) {
            this.employees = employees;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }
}
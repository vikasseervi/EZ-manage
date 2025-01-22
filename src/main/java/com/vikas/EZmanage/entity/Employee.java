package com.vikas.EZmanage.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "employeeCache")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Auth auth;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "employee_role",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public Employee() {}

    public Employee(Long id, Auth auth, String firstName, String lastName, String email, Set<Role> roles) {
        this.id = id;
        this.auth = auth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    private Employee(EmployeeBuilder builder) {
        this.id = builder.id;
        this.auth = builder.auth;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.roles = builder.roles;
    }

    // Helper method to add a role
    public void addRole(Role role) {
        this.roles.add(role);
        role.getEmployees().add(this);
    }

    // Helper method to remove a role
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getEmployees().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            this.roles = roles;
            for (Role role : roles) {
                addRole(role);
            }
        }
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", auth=" + auth +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class EmployeeBuilder {
        private Long id;
        private Auth auth;
        private String firstName;
        private String lastName;
        private String email;
        private Set<Role> roles;

        public EmployeeBuilder() {}

        public EmployeeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmployeeBuilder auth(Auth auth) {
            this.auth = auth;
            return this;
        }

        public EmployeeBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public EmployeeBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public EmployeeBuilder email(String email) {
            this.email = email;
            return this;
        }

        public EmployeeBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public static EmployeeBuilder builder() {
        return new EmployeeBuilder();
    }
}
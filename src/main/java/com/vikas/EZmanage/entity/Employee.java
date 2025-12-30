package com.vikas.EZmanage.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
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

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @JsonIgnore
    @Column(name = "password_hash", nullable = true, length = 68)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_role",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Employee() {}

    public Employee(Long id, String username, String password, Boolean active, String firstName, String lastName, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active == null ? true : active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles == null ? new HashSet<>() : roles;
        // maintain bidirectional links
        for (Role r : this.roles) {
            r.getEmployees().add(this);
        }
    }

    private Employee(EmployeeBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.active = builder.active == null ? true : builder.active;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.roles = builder.roles == null ? new HashSet<>() : builder.roles;
        for (Role r : this.roles) {
            r.getEmployees().add(this);
        }
    }

    // Helper method to add a role
    public void addRole(Role role) {
        if (roles == null) roles = new HashSet<>();
        if (role == null) return;
        if (roles.add(role)) {
            role.getEmployees().add(this);
        }
    }

    // Helper method to remove a role
    public void removeRole(Role role) {
        if (roles == null || role == null) return;
        if (roles.remove(role)) {
            role.getEmployees().remove(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        // clear existing bidirectional links
        if (this.roles != null) {
            for (Role r : new HashSet<>(this.roles)) {
                removeRole(r);
            }
        }
        if (roles != null) {
            for (Role r : roles) {
                addRole(r);
            }
        } else {
            this.roles = new HashSet<>();
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
                ", username='" + username + '\'' +
                ", active=" + active +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
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
        private String username;
        private String password;
        private Boolean active = true;
        private String firstName;
        private String lastName;
        private String email;
        private Set<Role> roles;

        public EmployeeBuilder() {}

        public EmployeeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EmployeeBuilder username(String username) {
            this.username = username;
            return this;
        }

        public EmployeeBuilder password(String password) {
            this.password = password;
            return this;
        }

        public EmployeeBuilder active(Boolean active) {
            this.active = active;
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
package com.vikas.EZmanage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_role",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "role_id"})})
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, referencedColumnName = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    private Role role;

    public EmployeeRole( ) {
    }

    public EmployeeRole(Long id, Employee employee, Role role) {
        this.id = id;
        this.employee = employee;
        this.role = role;
    }

    private EmployeeRole(EmployeeRoleBuilder builder) {
        this.id = builder.id;
        this.employee = builder.employee;
        this.role = builder.role;
    }

    public Long getId( ) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee( ) {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Role getRole( ) {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString( ) {
        return "EmployeeRole{" +
                "id=" + id +
                ", employeeId=" + employee +
                ", roleId=" + role +
                '}';
    }

    public static class EmployeeRoleBuilder {
        private Long id;
        private Employee employee;
        private Role role;

        public EmployeeRoleBuilder id(Long id) {
            this.id = id;
            return this;
        }
        public EmployeeRoleBuilder employee(Employee employee) {
            this.employee = employee;
            return this;
        }
        public EmployeeRoleBuilder role(Role role) {
            this.role = role;
            return this;
        }
        public EmployeeRole build() {
            return new EmployeeRole(this);
        }
    }
}

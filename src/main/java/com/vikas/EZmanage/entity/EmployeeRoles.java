package com.vikas.EZmanage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_roles",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "role_id"})})
public class EmployeeRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, referencedColumnName = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    private Roles role;

    public EmployeeRoles( ) {
    }

    public EmployeeRoles(Long id, Employee employee, Roles role) {
        this.id = id;
        this.employee = employee;
        this.role = role;
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

    public Roles getRole( ) {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @Override
    public String toString( ) {
        return "EmployeeRoles{" +
                "id=" + id +
                ", employeeId=" + employee +
                ", roleId=" + role +
                '}';
    }
}

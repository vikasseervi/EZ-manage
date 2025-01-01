package com.vikas.EZmanage.controller;

import com.vikas.EZmanage.dto.EmployeeRequest;
import com.vikas.EZmanage.entity.Auth;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.entity.EmployeeRole;
import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.service.AuthService;
import com.vikas.EZmanage.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/EZmanage")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuthService authService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private EmployeeRoleService employeeRoleService;
//
//    // --- AUTH ENDPOINTS ---
//    @PostMapping("/auth")
//    public ResponseEntity<Auth> createAuth(@RequestBody Auth auth) {
//        return ResponseEntity.ok(authService.save(auth));
//    }
//
//    @GetMapping("/auth/{id}")
//    public ResponseEntity<Auth> getAuthById(@PathVariable Long id) {
//        return ResponseEntity.ok(authService.findById(id));
//    }

    // --- EMPLOYEE ENDPOINTS ---
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest employee) {
        return ResponseEntity.ok(authService.signUp(employee));
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

//    // --- ROLE ENDPOINTS ---
//    @PostMapping("/roles")
//    public ResponseEntity<Role> createRole(@RequestBody Role role) {
//        return ResponseEntity.ok(roleService.save(role));
//    }
//
//    @GetMapping("/roles")
//    public ResponseEntity<List<Role>> getAllRoles() {
//        return ResponseEntity.ok(roleService.findAll());
//    }
//
//    @GetMapping("/roles/{id}")
//    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
//        return ResponseEntity.ok(roleService.findById(id));
//    }
//
//    // --- EMPLOYEE ROLES ENDPOINTS ---
//    @PostMapping("/employee-roles")
//    public ResponseEntity<EmployeeRole> assignRoleToEmployee(@RequestBody EmployeeRole employeeRole) {
//        return ResponseEntity.ok(employeeRoleService.save(employeeRole));
//    }
//
//    @GetMapping("/employee-roles/{employeeId}")
//    public ResponseEntity<List<EmployeeRole>> getRolesByEmployeeId(@PathVariable Long employeeId) {
//        return ResponseEntity.ok(employeeRoleService.findByEmployeeId(employeeId));
//    }
//
//    @DeleteMapping("/employee-roles/{id}")
//    public ResponseEntity<Void> deleteEmployeeRole(@PathVariable Long id) {
//        employeeRoleService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
}

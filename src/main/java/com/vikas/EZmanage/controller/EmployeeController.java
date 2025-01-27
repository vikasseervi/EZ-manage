package com.vikas.EZmanage.controller;

import com.vikas.EZmanage.dto.SignupRequestDTO;
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
@RequestMapping("/ezmanage/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    private AuthService authService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, AuthService authService) {
        this.employeeService = employeeService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(authService.signUp(signupRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody Employee updatedEmployee) {
        return ResponseEntity.ok(employeeService.update(employeeId, updatedEmployee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
//        return ResponseEntity.ok("Employee deleted successfully!");
        return ResponseEntity.noContent().build();
    }
}

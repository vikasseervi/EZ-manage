package com.vikas.EZmanage.controller;

import com.vikas.EZmanage.dto.EmployeeDTO;
import com.vikas.EZmanage.dto.SignupRequestDTO;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ezmanage/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(employeeService.signUp(signupRequestDTO));
    }

    @GetMapping
//    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody Employee updatedEmployee) {
        return ResponseEntity.ok(employeeService.update(employeeId, updatedEmployee));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
//        return ResponseEntity.ok("Employee deleted successfully!");
        return ResponseEntity.noContent().build();
    }
}

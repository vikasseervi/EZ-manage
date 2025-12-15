package com.vikas.EZmanage.service;

import com.vikas.EZmanage.dto.SignupRequestDTO;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.exception.ResourceNotFound;
import com.vikas.EZmanage.repository.EmployeeRepository;
import com.vikas.EZmanage.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee signUp(SignupRequestDTO signupRequestDTO) {
        Employee employee = new Employee();
        employee.setUsername(signupRequestDTO.getUsername());
        employee.setFirstName(signupRequestDTO.getFirstName());
        employee.setLastName(signupRequestDTO.getLastName());
        employee.setEmail(signupRequestDTO.getEmail());
        Role defaultRole = roleRepository.findByRoleName(Role.RoleName.ROLE_EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFound("Default role not found in DB"));
        employee.addRole(defaultRole);
        employee.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + id));
    }

    public Employee update(Long employeeId, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + employeeId));
        if(updatedEmployee.getFirstName() != null) employee.setFirstName(updatedEmployee.getFirstName());
        if(updatedEmployee.getLastName() != null) employee.setLastName(updatedEmployee.getLastName());
        if(updatedEmployee.getEmail() != null) employee.setEmail(updatedEmployee.getEmail());
        if(updatedEmployee.getRoles() != null) employee.setRoles(updatedEmployee.getRoles());
        if(updatedEmployee.getActive() != null) employee.setActive(updatedEmployee.getActive());
        if(updatedEmployee.getUsername() != null) employee.setUsername(updatedEmployee.getUsername());

        return employeeRepository.save(employee);
    }

    public void deleteById(Long id){
        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + id));
        employeeRepository.deleteById(id);
    }
}
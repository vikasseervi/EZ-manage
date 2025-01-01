package com.vikas.EZmanage.service;

import com.vikas.EZmanage.dto.EmployeeRequest;
import com.vikas.EZmanage.entity.Auth;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.repository.AuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthRepository authRepository, EmployeeService employeeService) {
        this.authRepository = authRepository;
        this.employeeService = employeeService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Employee signUp(EmployeeRequest employeeRequest){
        if(authRepository.findByUsername(employeeRequest.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists.");
        }

        String hashedPassword = passwordEncoder.encode(employeeRequest.getPassword());

        Auth auth = new Auth.AuthBuilder()
                .username(employeeRequest.getUsername())
                .passwordHash(hashedPassword)
                .build();

        authRepository.save(auth);

        Employee employee = new Employee.EmployeeBuilder()
                .auth(auth)
                .firstName(employeeRequest.getFirst_name())
                .lastName(employeeRequest.getLast_name())
                .email(employeeRequest.getEmail())
                .build();

        employeeService.save(employee);

        return employee;
    }

    public void saveEmployee(Employee employee) {

    }

}

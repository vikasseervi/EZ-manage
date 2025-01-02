package com.vikas.EZmanage.service;

import com.vikas.EZmanage.dto.SignupRequestDTO;
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

    public Employee signUp(SignupRequestDTO signupRequestDTO){
        if(authRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists.");
        }

        String hashedPassword = passwordEncoder.encode(signupRequestDTO.getPassword());

        Auth auth = new Auth.AuthBuilder()
                .username(signupRequestDTO.getUsername())
                .passwordHash(hashedPassword)
                .build();

        Employee employee = new Employee.EmployeeBuilder()
                .auth(auth)
                .firstName(signupRequestDTO.getFirst_name())
                .lastName(signupRequestDTO.getLast_name())
                .email(signupRequestDTO.getEmail())
                .build();

        employeeService.save(employee);

        return employee;
    }
}

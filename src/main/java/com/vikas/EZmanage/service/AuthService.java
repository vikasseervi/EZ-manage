package com.vikas.EZmanage.service;

import com.vikas.EZmanage.dto.LoginRequestDTO;
import com.vikas.EZmanage.dto.SignupRequestDTO;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.repository.EmployeeRepository;
import com.vikas.EZmanage.repository.RoleRepository;
import com.vikas.EZmanage.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final  JWTUtil jwtUtil;


    public AuthService(EmployeeRepository employeeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public void register(SignupRequestDTO dto) {

        employeeRepository.findByUsername(dto.getUsername())
            .ifPresent(u -> { throw new IllegalArgumentException("Username already taken"); });

        Employee employee = new Employee();
        employee.setUsername(dto.getUsername());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setActive(true);

        Role defaultRole = roleRepository
                .findByRoleName(Role.RoleName.ROLE_EMPLOYEE)
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_EMPLOYEE not found"));

        employee.getRoles().add(defaultRole);

        employeeRepository.save(employee);
    }

    public String login(LoginRequestDTO dto) {

        // if you're not using JWT or sessions manually, this alone is enough for basic auth flows
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // depending on JWT or session
        String JWTToken = jwtUtil.generateToken(auth.getName(), 15); // 15 minutes expiry
        return JWTToken;
    }
}


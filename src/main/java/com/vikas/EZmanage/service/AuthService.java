package com.vikas.EZmanage.service;

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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void signUp(String username, String password, String firstName, String lastName, String email){
        if(authRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        Auth auth = new Auth.AuthBuilder()
                .username(username)
                .passwordHash(password)
                .build();

//        authRepository.save(auth);

        Employee employee = new Employee.EmployeeBuilder()
                .auth(auth)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    public void saveEmployee(Employee employee) {

    }

}

package com.vikas.EZmanage.service;

import com.vikas.EZmanage.dto.CustomUserDetails;
import com.vikas.EZmanage.entity.Auth;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.repository.AuthRepository;
import com.vikas.EZmanage.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public CustomUserDetailsService(AuthRepository authRepository, EmployeeRepository employeeRepository) {
        this.authRepository = authRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Auth auth = authRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Employee employee = employeeRepository.findById(auth.getId()).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        return CustomUserDetails.builder()
                .username(auth.getUsername())
                .password(auth.getPasswordHash())
                .active(auth.getActive())
                .authorities(mapRolesToAuthorities(employee.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
    }
}

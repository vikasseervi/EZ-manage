package com.vikas.EZmanage.service;

import com.vikas.EZmanage.dto.EmployeeDTO;
import com.vikas.EZmanage.dto.SignupRequestDTO;
import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.exception.ResourceNotFound;
import com.vikas.EZmanage.repository.EmployeeRepository;
import com.vikas.EZmanage.repository.RoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeDTO save(Employee employee) {
        return mapToDTO(employeeRepository.save(employee));
    }

    @CacheEvict(value = {"employeeDtoCache", "employeeListCache"}, allEntries = true)
    public EmployeeDTO signUp(SignupRequestDTO signupRequestDTO) {
        Employee employee = new Employee();
        employee.setUsername(signupRequestDTO.getUsername());
        employee.setFirstName(signupRequestDTO.getFirstName());
        employee.setLastName(signupRequestDTO.getLastName());
        employee.setEmail(signupRequestDTO.getEmail());
        Role defaultRole = roleRepository.findByRoleName(Role.RoleName.ROLE_EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFound("Default role not found in DB"));
        employee.addRole(defaultRole);
        employee.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        return mapToDTO(employeeRepository.save(employee));
    }

    @Cacheable(value = "employeeListCache")
    public List<EmployeeDTO> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(this::mapToDTO).toList();
    }

    @Cacheable(value = "employeeDtoCache", key = "#id")
    public EmployeeDTO findById(Long id) {
//        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + id));
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + id));
        return mapToDTO(employee);
    }

    @CachePut(value = "employeeDtoCache", key = "#employeeId")
    public EmployeeDTO update(Long employeeId, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + employeeId));
        if(updatedEmployee.getFirstName() != null) employee.setFirstName(updatedEmployee.getFirstName());
        if(updatedEmployee.getLastName() != null) employee.setLastName(updatedEmployee.getLastName());
        if(updatedEmployee.getEmail() != null) employee.setEmail(updatedEmployee.getEmail());
        if(updatedEmployee.getRoles() != null) employee.setRoles(updatedEmployee.getRoles());
        if(updatedEmployee.getActive() != null) employee.setActive(updatedEmployee.getActive());
        if(updatedEmployee.getUsername() != null) employee.setUsername(updatedEmployee.getUsername());

        return mapToDTO(employeeRepository.save(employee));
    }

    @CacheEvict(value = {"employeeDtoCache", "employeeListCache"}, key = "#id")
    public void deleteById(Long id){
        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + id));
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setUsername(employee.getUsername());
        dto.setActive(employee.getActive());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setRoles(employee.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toSet()));
        return dto;
    }

    public Employee registerOrFetchOAuthUser(String email, String firstName, String lastName) {
        return employeeRepository.findByEmail(email)
                .orElseGet(() -> {
                    Employee newEmployee = new Employee();
                    newEmployee.setEmail(email);
                    newEmployee.setUsername(email);
                    newEmployee.setFirstName(firstName);
                    newEmployee.setLastName(lastName);
                    newEmployee.setActive(true);

                    roleRepository.findByRoleName(Role.RoleName.ROLE_EMPLOYEE).ifPresent(newEmployee::addRole);

                    return employeeRepository.save(newEmployee);
                });
    }
}
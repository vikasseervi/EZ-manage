package com.vikas.EZmanage.service;

import com.vikas.EZmanage.entity.Employee;
import com.vikas.EZmanage.exception.ResourceNotFound;
import com.vikas.EZmanage.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
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
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());

        return employeeRepository.save(employee);
    }

    public void deleteById(Long id){
        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Employee does not exists with given id : " + id));
        employeeRepository.deleteById(id);
    }
}

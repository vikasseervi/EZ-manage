package com.vikas.EZmanage.service;

import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.exception.ResourceNotFound;
import com.vikas.EZmanage.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository =roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Role does not exists with given id : " + id));
    }

    public void deleteById(Long id) {

        roleRepository.deleteById(id);
    }

    public Long findIdByRoleName(Role.RoleName roleName) {
        return roleRepository.findIdByRoleName(roleName);
    }

}

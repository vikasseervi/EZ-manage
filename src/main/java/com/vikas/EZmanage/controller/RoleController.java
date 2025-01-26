package com.vikas.EZmanage.controller;

import com.vikas.EZmanage.entity.Role;
import com.vikas.EZmanage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ezmanage/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable("id") Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

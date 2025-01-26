package com.vikas.EZmanage.repository;

import com.vikas.EZmanage.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r.id FROM Role r WHERE r.roleName = :roleName")
    Long findIdByRoleName(@Param("roleName") Role.RoleName roleName);

}

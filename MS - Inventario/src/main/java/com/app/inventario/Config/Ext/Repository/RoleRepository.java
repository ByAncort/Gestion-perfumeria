package com.app.inventario.Config.Ext.Repository;

import com.app.inventario.Config.Ext.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByName(String roleName);

    Optional<Role> findByName(String role_user);
}

package com.app.inventario.Config.Ext.Repository;

import com.app.inventario.Config.Ext.Models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String roleName);

    Optional<Permission> findByName(String name);
}

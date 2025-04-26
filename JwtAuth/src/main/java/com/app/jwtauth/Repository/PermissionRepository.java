package com.app.jwtauth.Repository;

import com.app.jwtauth.Models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String roleName);

    Optional<Permission> findByName(String name);
}

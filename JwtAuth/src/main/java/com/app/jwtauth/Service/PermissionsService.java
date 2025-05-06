package com.app.jwtauth.Service;

import com.app.jwtauth.Models.Permission;
import com.app.jwtauth.Repository.PermissionRepository;
import com.app.jwtauth.dto.PermissionDto;
import com.app.jwtauth.dto.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PermissionsService {

    private final PermissionRepository permissionRepository;
    private static final Logger logger = LoggerFactory.getLogger(PermissionsService.class);


    public List<Permission> getAllPermissions(){
        return permissionRepository.findAll();
    }
    public ServiceResult<Permission> savePermissions(PermissionDto request) {
        try {
            Permission savedPermission = permissionRepository.save(
                    Permission.builder()
                            .name(request.getName())
                            .build()
            );
            logger.info("Permission created successfully");
            return new ServiceResult<>(savedPermission);
        } catch (Exception e) {
            logger.error("Error creating Permission: ", e);
            List<String> errors = new ArrayList<>();
            errors.add("Error al crear el permiso: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }
    public ServiceResult<Permission> deletePermission(Long id) {
        try {
            Optional<Permission> optionalPermission = permissionRepository.findById(id);
            if (optionalPermission.isEmpty()) {
                logger.error("No existe el permiso con ID: {}", id);
                List<String> errors = new ArrayList<>();
                errors.add("Permiso no encontrado");
                return new ServiceResult<>(errors);
            } else {
                Permission permission = optionalPermission.get();
                permissionRepository.delete(permission);
                return new ServiceResult<>(permission);
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el permiso con ID: " + id, e);
            List<String> errors = new ArrayList<>();
            errors.add("Error al eliminar permiso");
            return new ServiceResult<>(errors);
        }
    }

}

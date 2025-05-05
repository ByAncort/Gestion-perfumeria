package com.app.jwtauth.Service;

import com.app.jwtauth.Models.Permission;
import com.app.jwtauth.Models.Role;
import com.app.jwtauth.Repository.PermissionRepository;
import com.app.jwtauth.Repository.RoleRepository;
import com.app.jwtauth.dto.RoleDto;
import com.app.jwtauth.dto.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;


    public ServiceResult<Role> create(RoleDto request) {
        try {
            Optional<Role> find = roleRepository.findByName(request.getName());

            if (find.isPresent()) {
                return new ServiceResult<>(List.of("Role with the same name already exists"));
            }
            Set<Permission> permissions = request.getPermissions().stream()
                    .map(dto -> permissionRepository.findByName(dto.getName())
                            .orElseThrow(() -> new RuntimeException("Permission not found: " + dto.getName())))
                    .collect(Collectors.toSet());

            Role create = Role.builder()
                    .name(request.getName())
                    .permissions(permissions)
                    .build();

            Role saved = roleRepository.save(create);
            return new ServiceResult<>(saved);

        } catch (Exception e) {
            return new ServiceResult<>(List.of("An error occurred while creating the role: " + e.getMessage()));
        }
    }

    public ServiceResult<String> deleteById(Long id) {
        try {
            Optional<Role> role = roleRepository.findById(id);
            if (role.isEmpty()) {
                return new ServiceResult<>(List.of("Role not found with ID: " + id));
            }

            roleRepository.deleteById(id);
            return new ServiceResult<>("Role deleted successfully.");
        } catch (Exception e) {
            return new ServiceResult<>(List.of("Error deleting role: " + e.getMessage()));
        }
    }

    public ServiceResult<Role> assignPermissionToRole(Long roleId, String permissionName) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));

            role.getPermissions().add(permission);
            Role updated = roleRepository.save(role);
            return new ServiceResult<>(updated);
        } catch (Exception e) {
            return new ServiceResult<>(List.of("Error assigning permission: " + e.getMessage()));
        }
    }
    public ServiceResult<Role> removePermissionFromRole(Long roleId, String permissionName) {
        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

            Permission permission = permissionRepository.findByName(permissionName)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));

            role.getPermissions().remove(permission);
            Role updated = roleRepository.save(role);
            return new ServiceResult<>(updated);
        } catch (Exception e) {
            return new ServiceResult<>(List.of("Error removing permission: " + e.getMessage()));
        }
    }


}

package com.app.jwtauth.Controller;

import com.app.jwtauth.Models.Role;
import com.app.jwtauth.Service.RoleService;
import com.app.jwtauth.dto.RoleDto;
import com.app.jwtauth.dto.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        ServiceResult<Role> result = roleService.create(roleDto);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        ServiceResult<String> result = roleService.deleteById(id);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.getErrors());
        }

        return ResponseEntity.ok(result.getData());
    }

    @PutMapping("/{roleId}/assign-permission")
    public ResponseEntity<?> assignPermission(@PathVariable Long roleId, @RequestParam String permissionName) {
        ServiceResult<Role> result = roleService.assignPermissionToRole(roleId, permissionName);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getErrors());
        }

        return ResponseEntity.ok(result.getData());
    }

    @PutMapping("/{roleId}/remove-permission")
    public ResponseEntity<?> removePermission(@PathVariable Long roleId, @RequestParam String permissionName) {
        ServiceResult<Role> result = roleService.removePermissionFromRole(roleId, permissionName);

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getErrors());
        }

        return ResponseEntity.ok(result.getData());
    }
}

package com.app.jwtauth.Controller;

import com.app.jwtauth.Models.Permission;
import com.app.jwtauth.Service.PermissionsService;
import com.app.jwtauth.dto.PermissionDto;
import com.app.jwtauth.dto.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/permission/")
public class PermissionController {
    private final PermissionsService permissionsService;

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestBody PermissionDto request) {
        ServiceResult<Permission> result = permissionsService.savePermissions(request);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        ServiceResult<Permission> result= permissionsService.deletePermission(id);
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getErrors());
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(result.getData());
        }
    }

    @GetMapping("get-all")
    public List<Permission> listPermissions(){
        return permissionsService.getAllPermissions();
    }
}

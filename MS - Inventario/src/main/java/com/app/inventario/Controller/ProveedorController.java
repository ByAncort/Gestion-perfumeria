package com.app.inventario.Controller;

import com.app.inventario.Dto.ProveedorDto;
import com.app.inventario.Dto.ServiceResult;
import com.app.inventario.Models.Proveedor;
import com.app.inventario.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ms-inventario/proveedor/")
@RequiredArgsConstructor
public class ProveedorController {
    private final ProveedorService proveedorService;


    @PostMapping("create-proveedor")
    public ResponseEntity<?> createProveedor(@RequestBody ProveedorDto proveedorDto){
        ServiceResult<Proveedor> result =proveedorService.addProveedor(proveedorDto);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getData());
        }
    }

}

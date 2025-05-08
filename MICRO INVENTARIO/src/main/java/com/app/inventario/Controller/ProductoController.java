package com.app.inventario.Controller;

import com.app.inventario.Dto.ProductoDto;
import com.app.inventario.Dto.ServiceResult;
import com.app.inventario.Models.Producto;
import com.app.inventario.Service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoDto dto) {
        ServiceResult<Producto> result = productoService.crearProducto(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{serial}")
    public ResponseEntity<?> buscarPorSerial(@PathVariable String serial) {
        ServiceResult<Producto> result = productoService.buscarPorSerial(serial);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Otros endpoints
}
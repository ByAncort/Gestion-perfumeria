package com.app.inventario.Controller;

import com.app.inventario.Dto.ServiceResult;
import com.app.inventario.Models.Producto;
import com.app.inventario.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QRController {
    private final ProductoService productoService;

//    @GetMapping("/escanear/{serial}")
//    public ResponseEntity<?> escanearProducto(@PathVariable String serial) {
//        ServiceResult<Producto> result = productoService.buscarPorSerial(serial);
//        if(result.hasErrors()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.getErrors());
//        }
//
//        Producto producto = result.getData();
//        Map<String, Object> response = new HashMap<>();
//        response.put("producto", producto);
//        response.put("inventario", getStockInfo(producto));
//
//        return ResponseEntity.ok(response);
//    }
//
//    private Map<String, Object> getStockInfo(Producto producto) {
//        // Lógica para obtener información de stock
//    }
}
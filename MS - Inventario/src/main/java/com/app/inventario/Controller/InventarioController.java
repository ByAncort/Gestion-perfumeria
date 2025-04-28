package com.app.inventario.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ms-inventario/")
@RequiredArgsConstructor
public class InventarioController {
    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Usuario autenticado: " + (auth != null ? auth.getName() : "NULO"));
    }
    @GetMapping("/est-connection")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("porfavor");
    }
}

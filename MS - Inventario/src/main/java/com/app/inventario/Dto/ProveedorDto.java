package com.app.inventario.Dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDto {
    private String nombre;
    private String rut;
    private String direccion;
    private String telefono;
    private String email;
    private boolean activo;
}

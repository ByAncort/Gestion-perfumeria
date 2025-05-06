package com.app.inventario.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDto {
    private Long id;
    private Long idProducto;
    private String productoNombre;
    private Long idSucursal;
    private String sucursalNombre;
    private int cantidad;
    private int stockMinimo;
    private LocalDateTime ultimaActualizacion;
}

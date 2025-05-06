package com.app.inventario.Dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private Long id;
    private String codigoSku;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal costo;
    private Long idCategoria;
    private String categoriaNombre;
    private boolean activo;
    private LocalDateTime fechaCreacion;
}
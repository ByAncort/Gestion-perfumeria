package com.app.inventario.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private int totalProductos;
}
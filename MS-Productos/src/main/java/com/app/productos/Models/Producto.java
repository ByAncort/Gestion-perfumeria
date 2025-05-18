package com.app.productos.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigoSku;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(unique = true, length = 50)
    private String serial;

    @Column(length = 50)
    private String catalogo;

    @Builder.Default
    private Boolean activo = true;

    @ElementCollection
    @CollectionTable(name = "producto_proveedor", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "proveedor_id")
    @Builder.Default
    private List<Long> proveedorIds = new ArrayList<>();

    @Column(name = "fecha_creacion")
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
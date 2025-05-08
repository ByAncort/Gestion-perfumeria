package com.app.inventario.Repository;

import com.app.inventario.Models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Para validación de SKU único
    boolean existsByCodigoSku(String codigoSku);

    // Para búsqueda por serial (QR)
    Optional<Producto> findBySerial(String serial);

    // Para validación de serial único
    boolean existsBySerial(String serial);

    // Búsqueda por catálogo
    List<Producto> findByCatalogo(String catalogo);
}

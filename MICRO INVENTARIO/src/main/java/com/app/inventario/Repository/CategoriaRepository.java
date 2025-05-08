package com.app.inventario.Repository;

import com.app.inventario.Models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Validar nombre único de categoría
    boolean existsByNombre(String nombre);

    // Búsqueda por nombre exacto
    Optional<Categoria> findByNombre(String nombre);
}
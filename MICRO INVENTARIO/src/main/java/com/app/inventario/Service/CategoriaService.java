package com.app.inventario.Service;

import com.app.inventario.Dto.*;
import com.app.inventario.Models.*;
import com.app.inventario.Repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public ServiceResult<Categoria> crearCategoria(CategoriaDto dto) {
        List<String> errors = new ArrayList<>();
        try {
            if(categoriaRepository.existsByNombre(dto.getNombre())) {
                errors.add("La categoría ya existe");
                return new ServiceResult<>(errors);
            }

            Categoria categoria = Categoria.builder()
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .build();

            return new ServiceResult<>(categoriaRepository.save(categoria));

        } catch(Exception e) {
            errors.add("Error: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }

    // Otros métodos (listar, actualizar, eliminar)
}
